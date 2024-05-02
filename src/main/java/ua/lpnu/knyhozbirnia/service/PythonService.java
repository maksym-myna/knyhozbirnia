package ua.lpnu.knyhozbirnia.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Semaphore;

@Service
public class PythonService {
    private static final String PYTHON_PATH = "C:/Python311/python.exe";
    private final Semaphore semaphore = new Semaphore(1);

    public final SseEmitter populate() throws InterruptedException {
        semaphore.acquire();
        try {
            String scriptDirectory = "m:/Personal/SE/Term 6/Data Warehouses/data population/";
            String scriptFile = "scripts/data_parser.py";
            return executePythonScript(scriptDirectory, scriptFile);
        } finally {
            semaphore.release();
        }
    }

    public SseEmitter performETL() throws InterruptedException {
        semaphore.acquire();
        try {
            String scriptDirectory = "M:/Personal/SE/Term 6/Data Warehouses/etl/";
            String scriptFile = "main.py";
            return executePythonScript(scriptDirectory, scriptFile);
        } finally {
            semaphore.release();
        }
    }

    private SseEmitter executePythonScript(String scriptDirectory, String scriptFile) {
        SseEmitter emitter = new SseEmitter(30_000_000L);

        new Thread(() -> {
            try {
                String scriptPath = scriptDirectory + scriptFile;
                ProcessBuilder processBuilder = new ProcessBuilder(PYTHON_PATH, scriptPath);
                processBuilder.directory(new File(scriptDirectory));
                Process process = processBuilder.start();

                emitter.onCompletion(process::destroy);
                emitter.onTimeout(process::destroy);

                // Read the output
                // Read the output in a separate thread
                new Thread(() -> {
                    try (BufferedReader outputReader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                        String line;
                        while ((line = outputReader.readLine()) != null) {
                            emitter.send(line);
                        }
                    } catch (IOException _) {
                    }
                }).start();

                // Read the error in a separate thread
                new Thread(() -> {
                    try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                        String errorLine;
                        while ((errorLine = errorReader.readLine()) != null) {
                            System.err.println(errorLine);
                        }
                    } catch (IOException _) {
                    }
                }).start();

                // Wait for the process to finish
                process.waitFor();

                emitter.complete();
                process.destroy();
            } catch (IOException | InterruptedException e) {
                emitter.completeWithError(e);
            }
        }).start();

        return emitter;
    }
}

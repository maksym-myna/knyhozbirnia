package ua.lpnu.knyhozbirnia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.lpnu.knyhozbirnia.model.Author;
import ua.lpnu.knyhozbirnia.model.Language;

import java.util.Optional;

@Repository
public interface LanguageRepository extends JpaRepository<Language, String> {
}

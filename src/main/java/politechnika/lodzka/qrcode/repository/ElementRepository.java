package politechnika.lodzka.qrcode.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import politechnika.lodzka.qrcode.model.scheme.Element;

@Repository
public interface ElementRepository extends JpaRepository<Element, Long> {
}

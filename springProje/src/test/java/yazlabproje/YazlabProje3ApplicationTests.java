package yazlabproje;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import yazlabproje.entity.Document;
import yazlabproje.repository.DocumentRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class YazlabProje3ApplicationTests {

	@Autowired
	private DocumentRepository repo;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	@Rollback(false)
	void testInsertDocument() throws IOException {
		File file = new File("C:\\Users\\sarea\\OneDrive\\Belgeler\\yazlab3_21_22_guz .pdf");
		Document document = new Document();
		document.setName(file.getName());
		byte[] bytes = Files.readAllBytes(file.toPath());
		document.setContent(bytes);
		long fileSize = bytes.length;
		document.setSize(bytes.length);
		document.setUploudTime(new Date());

		Document savedDoc = repo.save(document);

		Document existDoc = entityManager.find(Document.class, savedDoc.getId());

		assertThat(existDoc.getSize()).isEqualTo(fileSize);

	}

}

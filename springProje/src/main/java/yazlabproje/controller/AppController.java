package yazlabproje.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import yazlabproje.entity.Document;
import yazlabproje.repository.DocumentRepository;

@Controller
public class AppController {

	@Autowired
	private DocumentRepository repo;

	@GetMapping("/")
	public String viewHomePage(Model model) {
		List<Document> listDocs = repo.findAll();
		model.addAttribute("listDocs", listDocs);
		return "home";
	}

	@PostMapping("/upload")
	public String uploadFile(@RequestParam("document") MultipartFile multipartFile, RedirectAttributes ra)
			throws IOException {

		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

		Document document = new Document();
		document.setName(fileName);
		document.setContent(multipartFile.getBytes());
		document.setSize(multipartFile.getSize());
		document.setUploudTime(new Date());

		repo.save(document);
		ra.addFlashAttribute("message", "Dosya yüklemesi başarılı.");

		return "redirect:/";
	}

	@GetMapping("/download")
	public void downloadFile(@Param("id") Long id, HttpServletResponse response) throws Exception {
		Optional<Document> result = repo.findById(id);
		if (!result.isPresent()) {
			throw new Exception(id + "id ile belge bulunamadı");
		}

		Document document = result.get();
		response.setContentType("application/octec-stream");
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + document.getName();

		response.setHeader(headerKey, headerValue);

		ServletOutputStream outputS = response.getOutputStream();
		outputS.write(document.getContent());
		outputS.close();

	}

}

package emlakcepte.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import emlakcepte.model.Realty;
import emlakcepte.request.RealtyRequest;
import emlakcepte.service.RealtyService;

@RestController
@RequestMapping(value = "/realtyes")
public class RealtyController {

	@Autowired
	private RealtyService realtyService;

	// GET /realtyes
	@GetMapping
	public ResponseEntity<List<Realty>> getAll() {
		return ResponseEntity.ok(realtyService.getAll());
	}

	// POST /realtyes
	@PostMapping
	public ResponseEntity<Realty> create(@RequestBody RealtyRequest realtyRequest) {
		Realty realty = realtyService.create(realtyRequest);
		return new ResponseEntity<>(realty, HttpStatus.CREATED);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<List<Realty>> getAllByUserId(@PathVariable int id) {
		List<Realty> realtyes = realtyService.getAllById(id);
		return ResponseEntity.ok(realtyes);
	}
	
	@GetMapping(value = "/status/active")
	public ResponseEntity<List<Realty>> getAllActiveRealtyes() {
		List<Realty> realtyes = realtyService.getAllActiveRealtyes();
		return ResponseEntity.ok(realtyes);
	}

}

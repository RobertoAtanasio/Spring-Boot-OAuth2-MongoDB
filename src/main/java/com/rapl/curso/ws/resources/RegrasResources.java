package com.rapl.curso.ws.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rapl.curso.ws.domain.Regras;
import com.rapl.curso.ws.services.RegrasService;

@RestController
@RequestMapping("/api")
public class RegrasResources {

	@Autowired
	RegrasService regrasService;
	
	@GetMapping("/users/regras")
    public ResponseEntity<List<Regras>> findAll() {
		List<Regras> regras = regrasService.findAll();
        return ResponseEntity.ok().body(regras);
    }
}

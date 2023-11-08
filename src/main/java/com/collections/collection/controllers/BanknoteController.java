package com.collections.collection.controllers;

import com.collections.collection.entities.Banknote;
import com.collections.collection.entities.dtos.BanknoteResponse;
import com.collections.collection.services.BanknoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/banknotes")
public class BanknoteController {

    private final BanknoteService banknoteService;

    @Autowired
    public BanknoteController(BanknoteService banknoteService) {
        this.banknoteService = banknoteService;
    }

    @GetMapping("/")
    public ResponseEntity<List<BanknoteResponse>> getAllBanknotes() {
        List<BanknoteResponse> banknoteResponses = banknoteService.getAllBanknoteResponses();
        return ResponseEntity.ok(banknoteResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BanknoteResponse> getBanknoteResponseById(@PathVariable Long id) {
        BanknoteResponse banknoteResponse = banknoteService.getBanknoteResponseById(id);
        if (banknoteResponse != null) {
            return ResponseEntity.ok(banknoteResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    // Endpoint para actualizar un billete existente
    @PutMapping("/{id}")
    public ResponseEntity<Banknote> updateBanknote(
            @PathVariable Long id,
            @ModelAttribute Banknote banknoteDetails,
            @RequestParam("frontImage") MultipartFile frontImage,
            @RequestParam("backImage") MultipartFile backImage) {
        try {
            Banknote updatedBanknote = banknoteService.updateBanknote(id, banknoteDetails, frontImage, backImage);
            return ResponseEntity.ok(updatedBanknote);
        } catch (Exception e) {
            // Manejar excepción y enviar respuesta adecuada
            return ResponseEntity.status(500).body(null);
        }
    }

    // Endpoint para eliminar un billete
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBanknote(@PathVariable Long id) {
        try {
            banknoteService.deleteBanknote(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // Manejar excepción y enviar respuesta adecuada
            return ResponseEntity.status(500).body("Error al eliminar el billete: " + e.getMessage());
        }
    }

    @PostMapping("/")
    public ResponseEntity<Banknote> createBanknote(
            @ModelAttribute Banknote banknote,
            @RequestParam("frontImage") MultipartFile frontImage,
            @RequestParam("backImage") MultipartFile backImage) {
        try {
            Banknote newBanknote = banknoteService.createBanknote(banknote, frontImage, backImage);
            return ResponseEntity.ok(newBanknote);
        } catch (Exception e) {
            // Manejar excepción y enviar respuesta adecuada
            return ResponseEntity.status(500).build();
        }
    }
}

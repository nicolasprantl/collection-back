package com.collections.collection.services;

import com.collections.collection.entities.Banknote;
import com.collections.collection.entities.BanknoteImage;
import com.collections.collection.entities.dtos.BanknoteResponse;
import com.collections.collection.repositories.BanknoteImageRepository;
import com.collections.collection.repositories.BanknoteRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BanknoteService {

    private final BanknoteRepository banknoteRepository;
    private final BanknoteImageRepository banknoteImageRepository;
    private final ImageService imageService;

    @Autowired
    public BanknoteService(BanknoteRepository banknoteRepository,
                           BanknoteImageRepository banknoteImageRepository,
                           ImageService imageService) {
        this.banknoteRepository = banknoteRepository;
        this.banknoteImageRepository = banknoteImageRepository;
        this.imageService = imageService;
    }

    @Transactional
    public Banknote createBanknote(Banknote banknote, MultipartFile frontImage, MultipartFile backImage) throws Exception {
        // Primero, guardamos el billete sin las imágenes
        Banknote savedBanknote = banknoteRepository.save(banknote);

        // Luego, subimos las imágenes y creamos los registros de imagen
        String frontImageUrl = imageService.storeImage(frontImage);
        String backImageUrl = imageService.storeImage(backImage);

        // Creamos y guardamos el registro de BanknoteImage asociado
        BanknoteImage banknoteImage = new BanknoteImage();
        banknoteImage.setBanknote(savedBanknote);
        banknoteImage.setFrontImageUrl(frontImageUrl);
        banknoteImage.setBackImageUrl(backImageUrl);
        banknoteImageRepository.save(banknoteImage);

        // Asociamos la imagen al billete y actualizamos
        savedBanknote.setImage(banknoteImage);
        return banknoteRepository.save(savedBanknote);
    }

    public List<BanknoteResponse> getAllBanknoteResponses() {
        List<Banknote> banknotes = banknoteRepository.findAll();

        // Mapea los objetos Banknote a objetos BanknoteResponse
        List<BanknoteResponse> banknoteResponses = banknotes.stream()
                .map(this::mapToBanknoteResponse)
                .collect(Collectors.toList());

        return banknoteResponses;
    }

    private BanknoteResponse mapToBanknoteResponse(Banknote banknote) {
        BanknoteResponse response = new BanknoteResponse();
        response.setId(banknote.getId());
        response.setIssueDate(String.valueOf(banknote.getIssueDate()));
        response.setCountry(banknote.getCountry());
        response.setDenomination(banknote.getDenomination());
        response.setSeries(banknote.getSeries());
        response.setDescription(banknote.getDescription());

        // Supongamos que tienes una entidad BanknoteImage que almacena las URLs de las imágenes
        BanknoteImage image = banknote.getImage();
        if (image != null) {
            response.setFrontImageUrl(image.getFrontImageUrl());
            response.setBackImageUrl(image.getBackImageUrl());
        } else {
            // Si no hay imagen asociada, puedes establecer las URL de las imágenes como cadenas vacías o null
            response.setFrontImageUrl("");
            response.setBackImageUrl("");
        }

        return response;
    }

    public BanknoteResponse getBanknoteResponseById(Long id) {
        Banknote banknote = banknoteRepository.findById(id).orElse(null);
        if (banknote != null) {
            return mapToBanknoteResponse(banknote);
        } else {
            return null;
        }
    }


    @Transactional
    public Banknote updateBanknote(Long id, Banknote banknoteDetails, MultipartFile frontImage, MultipartFile backImage) throws Exception {
        Banknote banknote = banknoteRepository.findById(id).orElseThrow(() -> new Exception("Banknote not found"));

        // Actualizar los datos del billete
        banknote.setCountry(banknoteDetails.getCountry());
        banknote.setDenomination(banknoteDetails.getDenomination());
        banknote.setIssueDate(banknoteDetails.getIssueDate());
        banknote.setSeries(banknoteDetails.getSeries());
        banknote.setDescription(banknoteDetails.getDescription());

        // Subir nuevas imágenes si se proporcionan
        if (frontImage != null && !frontImage.isEmpty()) {
            String frontImageUrl = imageService.storeImage(frontImage);
            banknote.getImage().setFrontImageUrl(frontImageUrl);
        }

        if (backImage != null && !backImage.isEmpty()) {
            String backImageUrl = imageService.storeImage(backImage);
            banknote.getImage().setBackImageUrl(backImageUrl);
        }

        // Guardamos la actualización
        return banknoteRepository.save(banknote);
    }

    @Transactional
    public void deleteBanknote(Long id) {
        banknoteRepository.deleteById(id);
        // También podrías eliminar las imágenes asociadas desde Cloudinary si es necesario
    }

}

package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.ImageDao;
import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.models.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService {
    private final ImageDao imageDao;
    @Autowired
    public ImageServiceImpl(final ImageDao imageDao){
        this.imageDao = imageDao;
    }

    @Transactional
    @Override
    public Image createImage(byte[] data) {
        return imageDao.create(data);
    }

    @Override
    public Optional<Image> findById(long imageId){
        return imageDao.findById(imageId);
    }

    @Transactional
    @Override
    public void replaceImage(long id, byte[] data) { imageDao.replaceImage(id,data); }
}

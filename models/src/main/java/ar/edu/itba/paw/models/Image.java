package ar.edu.itba.paw.models;

import javax.persistence.*;

@Entity
@Table(name = "images")
public class Image {

    @Id
    @GeneratedValue(strategy =  GenerationType.SEQUENCE, generator ="images_image_id_seq" )
    @SequenceGenerator(sequenceName = "images_image_id_seq" , name = "images_image_id_seq", allocationSize = 1)
    @Column(name = "image_id")
    private Long imageId;

    @Column(name = "bytea")
    private byte[] data;

    protected Image() {

    }

    public Image(byte[] data) {
        this.data = data;
    }

    public Image(Long id, byte[] data) {
        this.imageId = id;
        this.data = data;
    }

    @Override
    public String toString() {
        return String.format("Image { imageId: %d }",
                imageId);
    }

    public Long getImageId() {
        return imageId;
    }

    public byte[] getData() {
        return data;
    }

}


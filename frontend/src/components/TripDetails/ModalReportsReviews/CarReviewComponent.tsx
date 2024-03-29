import styles from "./styles.module.scss";
import CircleImg from "@/components/img/circleImg/CircleImg.tsx";
import { Button } from "react-bootstrap";
import carModel from "@/models/CarModel.ts";
import tripModel from "@/models/TripModel.ts";
import useReviewsCar from "@/hooks/reportReview/useReviewsCar.tsx";
import { useTranslation } from "react-i18next";
import ImageService from "@/services/ImageService.ts";

export interface CarReviewComponentProps {
  car: carModel;
  trip: tripModel;
  openModalCar: () => void;
}

const CarReviewComponent = ({
  trip,
  car,
  openModalCar,
}: CarReviewComponentProps) => {
  const { t } = useTranslation();
  const { data: isReviewed, isLoading: isLoadingReview } = useReviewsCar(trip);

  const buttonClassName = isReviewed
    ? styles.userContainerReady
    : styles.userContainerReview;

  return (
    !isLoadingReview && (
      <div className={styles.marginCointainer}>
        <Button
          onClick={() => openModalCar()}
          disabled={isReviewed}
          className={buttonClassName}
        >
          <CircleImg src={ImageService.getSmallImageUrl(car.imageUri)} size={50} />
          <div className={styles.infoContainer}>
            <h4>{car.infoCar}</h4>
          </div>
        </Button>
        {isReviewed && (
          <div className={styles.aclaration_text}>
            <span>{t("trip_detail.review.reviewed")}</span>
          </div>
        )}
      </div>
    )
  );
};

export default CarReviewComponent;

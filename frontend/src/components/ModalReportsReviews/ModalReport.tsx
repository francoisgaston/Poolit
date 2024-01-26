import styles from "./styles.module.scss";
import {Button, Modal} from "react-bootstrap";
import PassangerReportReviewComponent from "@/components/ModalReportsReviews/PassangerReportReviewComponent.tsx";
import PassangerModel from "@/models/PassangerModel.ts";
import DriverReportReviewComponent from "@/components/ModalReportsReviews/DriverReportReviewComponent.tsx";
import userPublicModel from "@/models/UserPublicModel.ts";
import {useTranslation} from "react-i18next";

export interface ModalReportProps {
    closeModal: () => void;
    passangers: PassangerModel[];
    driver: userPublicModel;
    isDriver: boolean;
}

const ModalReport = ({ closeModal, passangers, driver, isDriver }: ModalReportProps) => {
    const { t } = useTranslation();

    return (
        <div className={styles.propProfile}>
            <Modal.Header closeButton>
                <Modal.Title><h2 className={styles.titleModal}>{t('modal.report.title')}</h2></Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <div className={styles.categoryContainer}>
                    {!isDriver &&
                    <div className={styles.passangerContainer}>
                        <div className={styles.titleContainer}>
                            <i className="bi bi-person-fill h3"></i>
                            <h3>{t('modal.report.driver')}</h3>
                        </div>
                        <DriverReportReviewComponent driver={driver} closeModal={closeModal}/>
                    </div>}
                    <div className={styles.passangerContainer}>
                        <div className={styles.titleContainer}>
                            <i className="bi bi-people-fill h3"></i>
                            <h3><h3>{t('modal.report.passangers')}</h3></h3>
                        </div>
                        <PassangerReportReviewComponent passanger={passangers[1]} closeModal={closeModal}/>
                    </div>
                </div>
            </Modal.Body>
            <Modal.Footer>
                <Button className={styles.backBtn} onClick={closeModal}>
                    {t('modal.report.close')}
                </Button>
            </Modal.Footer>
        </div>
    );
};

export default ModalReport;

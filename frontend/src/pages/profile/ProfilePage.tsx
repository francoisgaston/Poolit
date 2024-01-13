import ProfileImg from "@/components/profile/img/ProfileImg";
import styles from "./styles.module.scss";
import {useTranslation} from "react-i18next";
import ProfilePhoto from "@/images/descarga.jpeg";
import ProfileProp from "@/components/profile/prop/ProfileProp";
import ProfileStars from "@/components/profile/stars/ProfileStars";
import DriverList from "@/components/profile/ProfileLists/DriverList";
import PassengerList from "@/components/profile/ProfileLists/PassangerList";
import TabComponent from "@/components/tab/TabComponent";
import {useEffect, useState} from "react";
import CityModel from "@/models/CityModel.ts";
import CityService from "@/services/CityService.ts";

const ProfilePage = () => {
  const { t } = useTranslation();

  const user = {
    name: "Gaston Francois",
    email: "gfrancois@itba.edu.ar",
    phone: "3424394741",
    neighborhood: "Balvanera",
    language: "Español",
    trips: "5",
    rating_driver: 3.5,
    rating_passenger: 1.5,
  };

  const [city, setCity] = useState<CityModel|null >(null);


    useEffect(() => {
      CityService.getCityById("http://localhost:8080/paw-2023a-07/api/cities/12").then(response => {
        // Extraer el cuerpo de la respuesta Axios
        // Luego, llamar a setProfileInfo con el resultado
        setCity(response);
      })
    })


  return (
    <div className={styles.main_container}>
      <div className={styles.profileCard}>
        <ProfileImg src={ProfilePhoto} />
        <h3 className="text-center">{city?.name}</h3>
        <ProfileProp prop={t("profile.props.email")} text={user.email} />
        <ProfileProp prop={t("profile.props.phone")} text={user.phone} />
        <ProfileProp
          prop={t("profile.props.neighborhood")}
          text={user.neighborhood}
        />
        <ProfileProp prop={t("profile.props.language")} text={user.language} />
        <ProfileProp prop={t("profile.props.trips")} text={user.trips} />
        <ProfileStars
          prop={t("profile.props.rating_driver")}
          rating={user.rating_driver}
        />
        <ProfileStars
          prop={t("profile.props.rating_passenger")}
          rating={user.rating_passenger}
        />
      </div>

      <div className={styles.list_block}>
        <TabComponent
          right_title={t("roles.passenger")}
          right_component={<PassengerList />}
          left_title={t("roles.driver")}
          left_component={<DriverList />}
        />
      </div>
    </div>
  );
};

export default ProfilePage;

import { Trip } from "@/types/Trip";
import ListProfileContainer from "@/components/profile/list/ListProfileContainer";
import ShortReview from "@/components/review/shorts/ShortReview";
import CardTripProfile from "@/components/cardTrip/cardTripProfile/cardTripProfile";
import { publicsReviewsPath, reservedTripsPath } from "@/AppRouter";
import { useTranslation } from "react-i18next";
import {useEffect, useState} from "react";
import tripsService from "@/services/TripsService.ts";

const PassengerList = () => {
  const { t } = useTranslation();

  const [trip, setTrip] = useState<TripModel|null >(null);


  useEffect(() => {
    tripsService.getTripById("http://localhost:8080/paw-2023a-07/api/trips/4").then(response => {
      // Extraer el cuerpo de la respuesta Axios
      // Luego, llamar a setProfileInfo con el resultado
      setTrip(response);
    })
  })


  const data: Trip[] = [
    {
      tripId: 1,
      originCity: {
        name: "Balvanera",
      },
      originAddress: "Av independencia 2135",
      destinationCity: {
        name: "Parque Patricios",
      },
      destinationAddress: "Iguazu 341",
      dayOfWeekString: "Miercoles",
      startDateString: "Date string",
      endDateString: "Date string",
      travelInfoDateString: "travel info",
      startTimeString: "time",
      integerQueryTotalPrice: "10",
      decimalQueryTotalPrice: "00",
      queryIsRecurrent: false,
      car: {
        imageId: "imagen",
      },
    },
  ];

  const data2 = [
    {
      type: "type",
      comment: "comment",
      raiting: 2,
      formattedDate: "Date",
    },
    {
      type: "type",
      comment: "comment",
      raiting: 2,
      formattedDate: "Date",
    },
  ];

  return (
    <div>
      <ListProfileContainer
        title={trip?.originAddress}
        btn_footer_text={t("profile.lists.review_more")}
        empty_text={t("profile.lists.review_empty")}
        empty_icon={"book"}
        data={data2}
        component_name={ShortReview}
        link={publicsReviewsPath.replace(":id", String(5))}
      />
      <ListProfileContainer
        title={t("profile.lists.reserved_next_title")}
        btn_footer_text={t("profile.lists.reserved_next_more")}
        empty_text={t("profile.lists.reserved_next_empty")}
        empty_icon={"car-front-fill"}
        data={data}
        component_name={CardTripProfile}
        link={reservedTripsPath}
      />
      <ListProfileContainer
        title={t("profile.lists.reserved_prev_title")}
        btn_footer_text={t("profile.lists.reserved_prev_more")}
        empty_text={t("profile.lists.reserved_prev_empty")}
        empty_icon={"car-front-fill"}
        data={data}
        component_name={CardTripProfile}
        link={reservedTripsPath}
      />
    </div>
  );
};

export default PassengerList;

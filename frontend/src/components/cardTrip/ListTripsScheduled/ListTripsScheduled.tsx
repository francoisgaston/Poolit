import useTripsByUri from "@/hooks/trips/useTripsByUri.tsx";
import { useTranslation } from "react-i18next";
import PaginationComponentExtraData from "@/components/pagination/PaginationComponent/PaginationComponentExtraData.tsx";
import CardTripScheduled from "@/components/cardTrip/cardTripScheduled/cardTripScheduled.tsx";
import tripModel from "@/models/TripModel.ts";

export interface ListTripsScheduledProps {
  uri: string;
  empty_component: React.ReactNode;
  current_page: number;
  extraData: (trip: tripModel)=>{startDate: string, endDate: string};
}

const ListTripsScheduled = ({
  uri,
  empty_component,
  current_page,
  extraData
}: ListTripsScheduledProps) => {
  const { t } = useTranslation();

  return (
    <div>
      <PaginationComponentExtraData
          CardComponent={CardTripScheduled}
          extraData={extraData}
          uri={uri}
          current_page={current_page}
          useFuction={useTripsByUri}
          empty_component={empty_component}
          itemsName={t("trip.title")}
      />
    </div>
  );
};

export default ListTripsScheduled;

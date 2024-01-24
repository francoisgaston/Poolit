import styles from "./styles.module.scss";
import MainHeader from "@/components/utils/MainHeader";
import MainComponent from "@/components/utils/MainComponent";
import TabComponent from "@/components/tab/TabComponent";
import { useTranslation } from "react-i18next";
import { useLocation } from "react-router-dom";
import EmptyList from "@/components/emptyList/EmptyList";
import ListTripsScheduled from "@/components/cardTrip/ListTripsScheduled/ListTripsScheduled";
import { useCurrentUser } from "@/hooks/users/useCurrentUser.tsx";
import SpinnerComponent from "@/components/Spinner/Spinner";
import createPaginationUri from "@/functions/CreatePaginationUri.tsx";

const CreatedPage = () => {
  const { isLoading, currentUser } = useCurrentUser();
  const { t } = useTranslation();
  const { search } = useLocation();
  const time = new URLSearchParams(search).get("time");

  const page = new URLSearchParams(search).get("page");
  const currentPage = page == null ? 1 : parseInt(page, 10);
  const uriFutureTrips = isLoading || currentUser === undefined ? null : createPaginationUri(currentUser?.futureCreatedTripsUri, currentPage , 2);
  const uriPastTrips = isLoading || currentUser === undefined ? null : createPaginationUri(currentUser?.pastCreatedTripsUri, currentPage , 2);


  return (
    <MainComponent>
      <MainHeader title={t("created_trips.title")} />
      <div className={styles.container_tab}>
        <TabComponent
          right_title={t("created_trips.future")}
          right_component={
              uriFutureTrips == null ? (
              <SpinnerComponent />
            ) : (
              <ListTripsScheduled
                uri={uriFutureTrips}
                current_page={currentPage}
                empty_component={
                  <EmptyList
                    text={t("created_trips.empty")}
                    second_text={t("created_trips.more")}
                    icon={"car-front-fill"}
                  />
                }
              />
            )
          }
          left_title={t("created_trips.past")}
          left_component={
            uriPastTrips == null ? (
              <SpinnerComponent />
            ) : (
              <ListTripsScheduled
                uri={uriPastTrips}
                current_page={currentPage}
                empty_component={
                  <EmptyList
                    text={t("created_trips.empty")}
                    second_text={t("created_trips.more")}
                    icon={"car-front-fill"}
                  />
                }
              />
            )
          }
          active={time == "past" ? "left" : "right"}
        />
      </div>
    </MainComponent>
  );
};

export default CreatedPage;

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
import {INITIALPAGE, TRIPSPAGESIZE} from "@/enums/PaginationConstants.ts";

const CreatedPage = () => {
  const { isLoading, currentUser } = useCurrentUser();
  const { t } = useTranslation();
  const { search } = useLocation();
  const time = new URLSearchParams(search).get("time");

  const page = new URLSearchParams(search).get("page");
  const currentPage = page == null ? INITIALPAGE : parseInt(page, 10);

  return (
    <MainComponent>
      <MainHeader title={t("created_trips.title")} />
      <div className={styles.container_tab}>
        <TabComponent
          right_title={t("created_trips.future")}
          right_component={
              isLoading || currentUser === undefined? (
              <SpinnerComponent />
            ) : (
              <ListTripsScheduled
                uri={createPaginationUri(currentUser.futureCreatedTripsUri, currentPage , TRIPSPAGESIZE)}
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
              isLoading || currentUser === undefined ? (
              <SpinnerComponent />
            ) : (
              <ListTripsScheduled
                uri={createPaginationUri(currentUser?.pastCreatedTripsUri, currentPage , TRIPSPAGESIZE)}
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

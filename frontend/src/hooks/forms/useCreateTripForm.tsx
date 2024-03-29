import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router-dom";
import useSuccessToast from "../toasts/useSuccessToast";
import useQueryError from "../errors/useQueryError";
import {
  CreateTripForm,
  CreateTripFormSchema,
  CreateTripFormSchemaType,
} from "@/forms/CreateTripForm";
import TripsService from "@/services/TripsService";
import useForm, { SubmitHandlerReturnModel } from "./useForm";
import { defaultToastTimeout } from "@/components/toasts/ToastProps";
import { useEffect, useState } from "react";
import useTripByUri from "../trips/useTripByUri";
import CreateTripModel from "@/models/CreateTripModel";
import UnknownResponseError from "@/errors/UnknownResponseError";
import { createdTripsPath, tripDetailsPath } from "@/AppRouter";
import useDiscovery from "../discovery/useDiscovery";
import DiscoveryMissingError from "@/errors/DiscoveryMissingError";

const emptyTripUri = "";

const useCreateTripForm = () => {
  const [tripUri, setTripUri] = useState(emptyTripUri);
  const { t } = useTranslation();
  const navigate = useNavigate();
  const showSuccessToast = useSuccessToast();
  const onQueryError = useQueryError();
  const { discovery, isError: isDiscoveryError } = useDiscovery();
  const {
    isLoading: isTripLoading,
    trip,
    isError: isTripError,
  } = useTripByUri(tripUri);

  const onSubmit: SubmitHandlerReturnModel<
    CreateTripFormSchemaType,
    CreateTripModel
  > = async (data: CreateTripFormSchemaType) => {
    if (!discovery || isDiscoveryError) {
      throw new DiscoveryMissingError();
    }
    return await TripsService.createTrip(discovery.tripsUriTemplate, data);
  };

  const onSuccess = (data: CreateTripModel) => {
    showSuccessToast({
      title: t("create_trip.success.title"),
      message: t("create_trip.success.message"),
      timeout: defaultToastTimeout,
    });
    setTripUri(data.tripUri);
  };

  const onError = (error: Error) => {
    const title = t("create_trip.error.title");
    const timeout = defaultToastTimeout;
    const customMessages = {
      [UnknownResponseError.ERROR_ID]: "create_trip.error.default",
    };
    onQueryError({ error, title, timeout, customMessages });
  };

  useEffect(() => {
    if (isTripLoading) {
      return;
    }
    if (isTripError) {
      navigate(createdTripsPath, { replace: true });
      return;
    }
    if (trip) {
      navigate(tripDetailsPath.replace(":tripId", trip.tripId.toString()), {
        replace: true,
      });
    }
  }, [isTripLoading, isTripError, navigate, trip]);

  return useForm({
    form: CreateTripForm,
    formSchema: CreateTripFormSchema,
    onSubmit,
    onSuccess,
    onError,
  });
};

export default useCreateTripForm;

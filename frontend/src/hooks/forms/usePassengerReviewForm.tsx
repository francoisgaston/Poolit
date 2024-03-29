import TripModel from "@/models/TripModel";
import UserPublicModel from "@/models/UserPublicModel";
import { useTranslation } from "react-i18next";
import useSuccessToast from "../toasts/useSuccessToast";
import useQueryError from "../errors/useQueryError";
import useDiscovery from "../discovery/useDiscovery";
import { SubmitHandlerReturnModel } from "./useForm";
import { ReviewFormSchemaType } from "@/forms/ReviewForm";
import DiscoveryMissingError from "@/errors/DiscoveryMissingError";
import PassengerReviewsService from "@/services/PassengerReviewsService";
import { defaultToastTimeout } from "@/components/toasts/ToastProps";
import UnknownResponseError from "@/errors/UnknownResponseError";
import useReviewForm from "./useReviewForm";
import usePassengerReviewOptions from "../passanger/usePassengerReviewOptions";
import usePassangerByUri from "../passanger/usePassangerByUri";

interface PassengerReviewFormHookProps {
  passenger: UserPublicModel;
  trip: TripModel;
  onSuccess?: () => void;
  onError?: (error: Error) => void;
}

const usePassengerReviewForm = ({
  passenger,
  trip,
  onSuccess: onSuccessProp,
  onError: onErrorProp,
}: PassengerReviewFormHookProps) => {
  const { t } = useTranslation();
  const showSuccessToast = useSuccessToast();
  const onQueryError = useQueryError();
  const { invalidatePassangersState } = usePassangerByUri();
  const { discovery, isError: isDiscoveryError } = useDiscovery();

  const onSubmit: SubmitHandlerReturnModel<ReviewFormSchemaType, void> = async (
    data: ReviewFormSchemaType
  ) => {
    if (!discovery || isDiscoveryError) {
      throw new DiscoveryMissingError();
    }
    return await PassengerReviewsService.createReview(
      discovery.passengerReviewsUriTemplate,
      trip.tripId,
      passenger.userId,
      data
    );
  };

  const onSuccess = () => {
    showSuccessToast({
      title: t("passenger_review.success.title"),
      message: t("passenger_review.success.message"),
      timeout: defaultToastTimeout,
    });
    invalidatePassangersState();
    onSuccessProp?.();
  };

  const onError = (error: Error) => {
    const title = t("passenger_review.error.title");
    const timeout = defaultToastTimeout;
    const customMessages = {
      [UnknownResponseError.ERROR_ID]: t("passenger_review.error.default"),
    };
    onQueryError({ error, title, timeout, customMessages });
    onErrorProp?.(error);
  };

  return useReviewForm({
    useReviewOptions: usePassengerReviewOptions,
    onSubmit,
    onSuccess,
    onError,
  });
};

export default usePassengerReviewForm;

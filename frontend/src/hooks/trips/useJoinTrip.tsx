import {useMutation, useQueryClient} from "@tanstack/react-query";
import useQueryError from "@/hooks/errors/useQueryError.tsx";
import {defaultToastTimeout} from "@/components/toasts/ToastProps.ts";
import {useTranslation} from "react-i18next";
import tripModel from "@/models/TripModel.ts";
import {useSearchParams} from "react-router-dom";
import tripsService from "@/services/TripsService.ts";
import getFormattedDateTime from "@/functions/DateFormat.ts";
import joinTripModel from "@/models/JoinTripModel.ts";
import {parseTemplate} from "url-template";

const useJoinTrip = (trip: tripModel) => {
    const onQueryError = useQueryError();
    const { t } = useTranslation();
    const queryClient = useQueryClient();
    const [params] = useSearchParams();
    const startDateTime = params.get("startDateTime") || "";
    const endDateTime = params.get("endDateTime") || "";

    const invalidatePassangersState = () => {
        queryClient.invalidateQueries({ queryKey: ['passangers'] });
        queryClient.invalidateQueries({ queryKey: ['tripDetails'] });
    }

    const mutation = useMutation({
        mutationFn: async () => {
            const data: joinTripModel = {
                startDate: getFormattedDateTime(startDateTime).date ,
                startTime: getFormattedDateTime(startDateTime).time,
                endDate: endDateTime==startDateTime? undefined : getFormattedDateTime(startDateTime).date
            }
            const uri = parseTemplate(trip?.passengersUriTemplate as string).expand({
                userId: null,
                startDateTime: null,
                endDateTime: null,
                passengerState: null,
            });
            return await tripsService.postJoinTrip(uri as string, data)
        },
        onError: (error: Error) => {
            onQueryError({
                error,
                title: t("passangers.error.title"),
                timeout: defaultToastTimeout,
            });
        },
        onSuccess: () => {
            invalidatePassangersState()
            console.log("passenger accepted")
        }
    })

    const onSubmit = () => {
        mutation.mutate()
    }

    return {onSubmit, invalidatePassangersState, ...mutation}

}

export default useJoinTrip;
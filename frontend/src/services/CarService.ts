import Service from "@/services/Service.ts";
import CarModel from "@/models/CarModel.ts";
import CarApi from "@/api/CarApi.ts";
import UserPrivateModel from "@/models/UserPrivateModel";
import { CreateCarFormSchemaType } from "@/forms/CreateCarForm";
import CarReviewModel from "@/models/CarReviewModel";
import PaginationModel from "@/models/PaginationModel";
import { EditCarFormSchemaType } from "@/forms/EditCarForm";
import CarReviewOptionModel from "@/models/CarReviewOptionModel";
import { ReviewFormSchemaType } from "@/forms/ReviewForm";
import { getCarReviewOptionsByRating } from "@/enums/carReviewsOptions.ts";

class CarService extends Service {
  public static getCarsByUser = async (
    uriTemplate: string,
    user: UserPrivateModel
  ): Promise<CarModel[]> => {
    return await this.resolveQuery(CarApi.getCarsByUser(uriTemplate, user));
  };

  public static getCarByUri = async (uri: string): Promise<CarModel> => {
    return await this.resolveQuery(CarApi.getCarByUri(uri));
  };

  public static getCarById = async (
    uriTemplate: string,
    id: string
  ): Promise<CarModel> => {
    return await this.resolveQuery(CarApi.getCarById(uriTemplate, id));
  };

  public static getCarReviews = async (
    uri: string
  ): Promise<PaginationModel<CarReviewModel>> => {
    return await this.resolveQuery(CarApi.getCarReviews(uri));
  };

  private static updateCarImage = async (
    uri: string,
    image: File
  ): Promise<void> => {
    if (!image || image.size === 0) {
      return;
    }
    await this.resolveQuery(CarApi.updateCarImage(uri, image));
  };

  public static createCar = async (
    uriTemplate: string,
    data: CreateCarFormSchemaType
  ): Promise<void> => {
    const { carUri } = await this.resolveQuery(
      CarApi.createCar(uriTemplate, data)
    );
    if (data.image && data.image.size > 0) {
      const car = await this.resolveQuery(CarApi.getCarByUri(carUri));
      await this.updateCarImage(car.imageUri, data.image);
    }
  };

  public static updateCar = async (
    car: CarModel,
    data: EditCarFormSchemaType
  ): Promise<void> => {
    await this.resolveQuery(CarApi.updateCar(car.selfUri, data));
    await this.updateCarImage(car.imageUri, data.image);
  };

  public static getCarReviewOptionsByRating = async (
    rating: number
  ): Promise<CarReviewOptionModel[]> => {
    return getCarReviewOptionsByRating(rating);
  };

  public static createCarReview = async (
    uri: string,
    tripId: number,
    data: ReviewFormSchemaType
  ): Promise<void> => {
    await this.resolveQuery(CarApi.createCarReview(uri, tripId, data));
  };
}

export default CarService;

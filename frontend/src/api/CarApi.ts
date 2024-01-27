import AxiosApi from "@/api/axios/AxiosApi.ts";
import { AxiosPromise, AxiosResponse } from "axios";
import CarModel from "@/models/CarModel.ts";
import UserPrivateModel from "@/models/UserPrivateModel";
import { CreateCarFormSchemaType } from "@/forms/CreateCarForm";
import { parseTemplate } from "url-template";

class CarApi extends AxiosApi {
  public static getCarsByUser: (
    user: UserPrivateModel
  ) => AxiosPromise<CarModel[]> = (user: UserPrivateModel) => {
    return this.get<CarModel[]>(user.carsUri);
  };

  public static getCarById: (uri: string) => AxiosPromise<CarModel> = (
    uri: string
  ) => {
    return this.get<CarModel>(uri, {
      headers: {},
    });
  };

  public static createCar: (
    uriTemplate: string,
    data: CreateCarFormSchemaType
  ) => AxiosPromise<void> = (
    uriTemplate: string,
    { car_plate, car_brand, car_description, seats, car_features = [], image }
  ) => {
    const uri = parseTemplate(uriTemplate).expand({});
    return this.post(
      uri,
      {
        plate: car_plate,
        carBrand: car_brand,
        carInfo: car_description,
        seats: seats,
        features: car_features,
      },
      {
        headers: {
          "Content-Type": "application/vnd.car.v1+json",
        },
      }
    ).then((response: AxiosResponse) => {
      const carUri = response.headers.location as string;
      if (carUri && image) {
        return this.updateCarImageWithCarUri(carUri, image);
      }
      return response;
    });
  };

  private static updateCarImageWithCarUri: (
    carUri: string,
    image: File
  ) => AxiosPromise<void> = (carUri: string, image: File) => {
    const formData = new FormData();
    formData.append("image", image);
    // TODO: Do not concatenate the uri
    return this.put(`${carUri}/image`, formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
  };
}

export default CarApi;

import userPrivateModel from "@/models/UserPrivateModel.ts";
import BaseMock from "@/__test__/mocks/BaseMock.ts";
import userPublicModel from "@/models/UserPublicModel.ts";
import userDriverModel from "@/models/UserDriverModel.ts";

export const privateUserRoleUser: userPrivateModel = {
  driverRating: 3.0,
  imageUri: "http://localhost:8080/paw-2023a-07/api/users/50/image",
  passengerRating: 0.0,
  reviewsDriverUri:
    "http://localhost:8080/paw-2023a-07/api/driver-reviews?forUser=50",
  reviewsPassengerUri:
    "http://localhost:8080/paw-2023a-07/api/passenger-reviews?forUser=50",
  selfUri: "http://localhost:8080/paw-2023a-07/api/users/50",
  surname: "Mentasti",
  tripCount: 4,
  userId: 50,
  username: "Jose Rodolfo",
  email: "jmentasti+testapi@itba.edu.ar",
  phone: "1139150686",
  carsUri: "http://localhost:8080/paw-2023a-07/api/cars?fromUser=50",
  cityUri: "http://localhost:8080/paw-2023a-07/api/cities/5",
  futureCreatedTripsUri:
    "http://localhost:8080/paw-2023a-07/api/trips?createdBy=50&past=false",
  futureReservedTripsUri:
    "http://localhost:8080/paw-2023a-07/api/trips?reservedBy=50&past=false",
  mailLocale: "en",
  pastCreatedTripsUri:
    "http://localhost:8080/paw-2023a-07/api/trips?createdBy=50&past=true",
  pastReservedTripsUri:
    "http://localhost:8080/paw-2023a-07/api/trips?reservedBy=50&past=true",
  recommendedTripsUri:
    "http://localhost:8080/paw-2023a-07/api/trips?recommendedFor=50",
  reportsApproved: 0,
  reportsPublished: 1,
  reportsReceived: 0,
  reportsRejected: 1,
  role: "USER",
};
export const privateUserRoleDriver: userPrivateModel = {
  driverRating: 3.0,
  imageUri: "http://localhost:8080/paw-2023a-07/api/users/50/image",
  passengerRating: 0.0,
  reviewsDriverUri:
    "http://localhost:8080/paw-2023a-07/api/driver-reviews?forUser=50",
  reviewsPassengerUri:
    "http://localhost:8080/paw-2023a-07/api/passenger-reviews?forUser=50",
  selfUri: "http://localhost:8080/paw-2023a-07/api/users/50",
  surname: "Mentasti",
  tripCount: 4,
  userId: 50,
  username: "Jose Rodolfo",
  email: "jmentasti+testapi@itba.edu.ar",
  phone: "1139150686",
  carsUri: "http://localhost:8080/paw-2023a-07/api/cars?fromUser=50",
  cityUri: "http://localhost:8080/paw-2023a-07/api/cities/5",
  futureCreatedTripsUri:
    "http://localhost:8080/paw-2023a-07/api/trips?createdBy=50&past=false",
  futureReservedTripsUri:
    "http://localhost:8080/paw-2023a-07/api/trips?reservedBy=50&past=false",
  mailLocale: "en",
  pastCreatedTripsUri:
    "http://localhost:8080/paw-2023a-07/api/trips?createdBy=50&past=true",
  pastReservedTripsUri:
    "http://localhost:8080/paw-2023a-07/api/trips?reservedBy=50&past=true",
  recommendedTripsUri:
    "http://localhost:8080/paw-2023a-07/api/trips?recommendedFor=50",
  reportsApproved: 0,
  reportsPublished: 1,
  reportsReceived: 0,
  reportsRejected: 1,
  role: "DRIVER",
};
export const privateUserRoleAdmin: userPrivateModel = {
  driverRating: 3.0,
  imageUri: "http://localhost:8080/paw-2023a-07/api/users/50/image",
  passengerRating: 0.0,
  reviewsDriverUri:
    "http://localhost:8080/paw-2023a-07/api/driver-reviews?forUser=50",
  reviewsPassengerUri:
    "http://localhost:8080/paw-2023a-07/api/passenger-reviews?forUser=50",
  selfUri: "http://localhost:8080/paw-2023a-07/api/users/50",
  surname: "Mentasti",
  tripCount: 4,
  userId: 50,
  username: "Gaston",
  email: "jmentasti+testapi@itba.edu.ar",
  phone: "1139150686",
  carsUri: "http://localhost:8080/paw-2023a-07/api/cars?fromUser=50",
  cityUri: "http://localhost:8080/paw-2023a-07/api/cities/5",
  futureCreatedTripsUri:
    "http://localhost:8080/paw-2023a-07/api/trips?createdBy=50&past=false",
  futureReservedTripsUri:
    "http://localhost:8080/paw-2023a-07/api/trips?reservedBy=50&past=false",
  mailLocale: "en",
  pastCreatedTripsUri:
    "http://localhost:8080/paw-2023a-07/api/trips?createdBy=50&past=true",
  pastReservedTripsUri:
    "http://localhost:8080/paw-2023a-07/api/trips?reservedBy=50&past=true",
  recommendedTripsUri:
    "http://localhost:8080/paw-2023a-07/api/trips?recommendedFor=50",
  reportsApproved: 0,
  reportsPublished: 1,
  reportsReceived: 0,
  reportsRejected: 1,
  role: "ADMIN",
};

export const publicUser: userPublicModel = {
  driverRating: 3.0,
  imageUri: "http://localhost:8080/paw-2023a-07/api/users/50/image",
  passengerRating: 0.0,
  reviewsDriverUri:
    "http://localhost:8080/paw-2023a-07/api/driver-reviews?forUser=50",
  reviewsPassengerUri:
    "http://localhost:8080/paw-2023a-07/api/passenger-reviews?forUser=50",
  selfUri: "http://localhost:8080/paw-2023a-07/api/users/50",
  surname: "Mentasti",
  tripCount: 4,
  userId: 50,
  username: "Jose Rodolfo",
};

export const driverUser: userDriverModel = {
  driverRating: 3.0,
  imageUri: "http://localhost:8080/paw-2023a-07/api/users/50/image",
  passengerRating: 0.0,
  reviewsDriverUri:
    "http://localhost:8080/paw-2023a-07/api/driver-reviews?forUser=50",
  reviewsPassengerUri:
    "http://localhost:8080/paw-2023a-07/api/passenger-reviews?forUser=50",
  selfUri: "http://localhost:8080/paw-2023a-07/api/users/50",
  surname: "Mentasti",
  tripCount: 4,
  userId: 50,
  username: "Jose Rodolfo",
  email: "jmentasti+testapi@itba.edu.ar",
  phone: "1139150686",
};

export const profileListInfo = {
  title: "Title component",
  btn_footer_text: "btn footer text",
  empty_text: "Empty text",
  empty_icon: "book",
  id: 5,
};

class UserMock extends BaseMock {
  public static getByIdPrivateRoleAdmin() {
    return this.get("/users/:userId", () =>
      this.jsonResponse(privateUserRoleAdmin, { status: this.OK_STATUS })
    );
  }
  public static getByIdPrivateRoleUser() {
    return this.get("/users/:userId", () =>
      this.jsonResponse(privateUserRoleUser, { status: this.OK_STATUS })
    );
  }
  public static optionsMock() {
    return this.options("/users/:userId", () =>
      this.plainResponse({
        status: this.OK_STATUS,
        headers: {
          "Access-Control-Allow-Methods": "GET, POST, PUT, DELETE, OPTIONS",
          "Access-Control-Allow-Origin": "*",
          "Access-Control-Expose-Headers":
            "JWT-authorization, JWT-refresh-authorization, Account-verification, Accept, X-Total-Pages, Link, Location",
        },
      })
    );
  }
  public static getByIdPrivateRoleDriver() {
    return this.get("/users/:userId", () =>
      this.jsonResponse(privateUserRoleDriver, { status: this.OK_STATUS })
    );
  }
  public static getByIdPublic() {
    return this.get("/users/:userId", () =>
      this.jsonResponse(publicUser, { status: this.OK_STATUS })
    );
  }

  public static getByIdDriver() {
    return this.get("/users/:userId", () =>
      this.jsonResponse(driverUser, { status: this.OK_STATUS })
    );
  }

  public static getEmptyUserImage() {
    return this.get("/users/:userId/image", () =>
      this.plainResponse({ status: this.NO_CONTENT_STATUS })
    );
  }

  public static createUserSuccess() {
    return this.post("/users", () =>
      this.plainResponse({ status: this.CREATED_STATUS })
    );
  }

  public static createUserEmailAlreadyExists() {
    return this.post("/users", () =>
      this.plainResponse({ status: this.CONFLICT_STATUS })
    );
  }

  public static updateUserSuccess() {
    return this.patch("/users/:userId", () =>
      this.plainResponse({ status: this.NO_CONTENT_STATUS })
    );
  }

  public static updateUserFail() {
    return this.patch("/users/:userId", () =>
      this.plainResponse({ status: this.INTERNAL_SERVER_ERROR_STATUS })
    );
  }

  public static updateUserImage() {
    return this.put("/users/:userId/image", () =>
      this.plainResponse({ status: this.NO_CONTENT_STATUS })
    );
  }
}

export default UserMock;

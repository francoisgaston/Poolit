import { AxiosError, AxiosRequestConfig, AxiosResponse } from "axios";
import Jwt from "./Jwt";
import Axios from "./axios";
import CitiesApi from "../CitiesApi";

const unauthorizedHttpStatusCode = 401;

export const AxiosResponseInterceptor = (response: AxiosResponse) => {
  const authToken = response.headers.authorization;
  const refreshToken = response.headers["authorization-refresh"];
  if (authToken) {
    Jwt.storeAuthToken(authToken);
  }
  if (refreshToken) {
    Jwt.storeRefreshToken(refreshToken);
  }
  return response;
};

export const AxiosResponseErrorInterceptor = async (error: AxiosError) => {
  if (error.response?.status === unauthorizedHttpStatusCode) {
    const refreshToken = Jwt.getRefreshToken();
    if (refreshToken) {
      await CitiesApi.getOptions({
        headers: {
          Authorization: refreshToken,
        },
      });
      const authToken = Jwt.getAuthToken();
      if (authToken) {
        const prevRequest = error.config as AxiosRequestConfig;
        prevRequest.headers = {
          ...(prevRequest.headers || {}),
          Authorization: authToken,
        };
        return Axios(prevRequest);
      }
    }
  }
  return Promise.reject(error);
};

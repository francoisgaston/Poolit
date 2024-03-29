import UsersApi from "@/api/UsersApi";
import Service from "./Service";
import UserPrivateModel from "@/models/UserPrivateModel";
import { RegisterFormSchemaType } from "@/forms/RegisterForm";
import { LoginFormSchemaType } from "@/forms/LoginForm";
import UserPublicModel from "@/models/UserPublicModel.ts";
import { EditProfileFormSchemaType } from "@/forms/EditProfileForm";
import UserDriverModel from "@/models/UserDriverModel.ts";

class UserService extends Service {
  public static login = async (data: LoginFormSchemaType) => {
    const { email, password, remember_me: rememberMe } = data;
    await this.resolveQuery(UsersApi.login(email, password, !!rememberMe));
  };

  public static logout = async () => {
    UsersApi.logout();
  };

  public static verifyAccount = async (email: string, token: string) => {
    await this.resolveQuery(UsersApi.verifyAccount(email, token));
  };

  public static register = async (
    uriTemplate: string,
    registerForm: RegisterFormSchemaType
  ) => {
    await this.resolveQuery(UsersApi.createUser(uriTemplate, registerForm));
  };

  public static getCurrentUser = async (): Promise<UserPrivateModel> =>
    await this.resolveQuery(UsersApi.getCurrentUser());

  public static getDriverUser = async (uri: string): Promise<UserDriverModel> =>
    await this.resolveQuery(UsersApi.getDriverByUri(uri));

  public static getUserByUri = async (uri: string): Promise<UserPublicModel> =>
    await this.resolveQuery(UsersApi.getPublicUser(uri));

  private static updateUserImage = async (uri: string, image: File) => {
    if (!image || image.size === 0) {
      return;
    }
    await this.resolveQuery(UsersApi.updateUserImage(uri, image));
  };

  public static updateUser = async (
    user: UserPrivateModel,
    data: EditProfileFormSchemaType
  ): Promise<void> => {
    await this.resolveQuery(UsersApi.updateUser(user.selfUri, data));
    await this.updateUserImage(user.imageUri, data.image);
  };

  public static getPrivateUserByUri = async (
    uri: string
  ): Promise<UserPrivateModel> =>
    await this.resolveQuery(UsersApi.getPrivateUser(uri));

  public static updateUserRoleToDriver = async (
    uriTemplate: string,
    user: UserPrivateModel
  ) => {
    await this.resolveQuery(UsersApi.updateUserRoleToDriver(uriTemplate, user));
  };
}

export default UserService;

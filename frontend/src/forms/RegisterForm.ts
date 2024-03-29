import CityFormField from "./fields/CityFormField";
import ConfirmPasswordField from "./fields/ConfirmPasswordFormField";
import EmailFormField from "./fields/EmailFormField";
import LastNameFormField from "./fields/LastNameFormField";
import LocaleFormField from "./fields/LocaleFormField";
import NameFormField from "./fields/NameFormField";
import PasswordFormField from "./fields/PasswordFormField";
import TelephoneFormField from "./fields/TelephoneFormField";
import Form, { InferedFormSchemaType } from "./Form";
import equalRefine from "./refine/EqualRefine";

const nameFieldName = "name";
const lastNameFieldName = "last_name";
const emailFieldName = "email";
const telephoneFieldName = "telephone";
const passwordFieldName = "password";
const confirmPasswordFieldName = "confirm_password";
const cityFieldName = "city";
const localeFieldName = "locale";

const RegisterFormFields = {
  [nameFieldName]: NameFormField,
  [lastNameFieldName]: LastNameFormField,
  [emailFieldName]: EmailFormField,
  [telephoneFieldName]: TelephoneFormField,
  [passwordFieldName]: PasswordFormField,
  [confirmPasswordFieldName]: ConfirmPasswordField,
  [cityFieldName]: CityFormField,
  [localeFieldName]: LocaleFormField,
};

export type RegisterFormFieldsType = typeof RegisterFormFields;

export const RegisterForm = new Form<RegisterFormFieldsType>(
  RegisterFormFields
);

export type RegisterFormSchemaType =
  InferedFormSchemaType<RegisterFormFieldsType>;

export const RegisterFormSchema = RegisterForm.getSchema().refine(
  equalRefine<RegisterFormSchemaType>(
    passwordFieldName,
    confirmPasswordFieldName
  ),
  {
    message: `error.${confirmPasswordFieldName}.invalid`,
    path: [confirmPasswordFieldName],
  }
);

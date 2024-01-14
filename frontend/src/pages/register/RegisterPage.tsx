import { useTranslation } from "react-i18next";
import styles from "./styles.module.scss";
import RegisterBanner from "@/images/register-banner.jpg";
import { Link } from "react-router-dom";
import { loginPath } from "@/AppRouter";
import RedirectHomeLogo from "@/components/links/RedirectHomeLogo/RedirectHomeLogo";
import RegisterForm from "./RegisterForm";
import useGetAllCities from "@/hooks/cities/useGetAllCities";

const RegisterPage = () => {
  const { isLoading, data: cities, isError } = useGetAllCities();
  const { t } = useTranslation();

  // Todo: Create loading component
  if (isLoading) return <div>Loading...</div>;

  // Todo: Create error component
  if (isError || !cities) return <div>Error</div>;

  return (
    <div className={styles.mainContainer}>
      <div className={styles.bannerContainer}>
        <img
          src={RegisterBanner}
          alt={t("register.banner")}
          className={styles.banner}
        />
        <div className={styles.logoContainer}>
          <RedirectHomeLogo />
        </div>
      </div>
      <div className={styles.formContainer}>
        <RegisterForm cities={cities} />
        <div className={styles.footerContainer}>
          <hr className="light-text mt-auto" />
          <div className={styles.loginContainer}>
            <h5 className={styles.title + " light-text"}>
              {t("register.already_registered")}
            </h5>
            <Link to={loginPath}>
              <h5 className="link-text fw-bold">{t("login.login")}</h5>
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
};

export default RegisterPage;

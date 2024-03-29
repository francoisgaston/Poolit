import styles from "./styles.module.scss";
import { useTranslation } from "react-i18next";
import { Link, useLocation, useNavigate } from "react-router-dom";
import "./navbarStyles.css"; // Importa el archivo CSS auxiliar
import PoolitLogo from "@/images/poolit.svg";
import CircleImg from "../img/circleImg/CircleImg";
import {
  registerPath,
  loginPath,
  homePath,
  profilePath,
  createdTripsPath,
  reservedTripsPath,
  createTripsPath,
  adminPath,
} from "@/AppRouter";
import Button from "react-bootstrap/Button";
import Dropdown from "react-bootstrap/Dropdown";
import { useState } from "react";
import useLogout from "@/hooks/auth/useLogout";
import useAuthentication from "@/hooks/auth/useAuthentication";
import { useCurrentUser } from "@/hooks/users/useCurrentUser.tsx";
import UsersRoles from "@/enums/UsersRoles";
import LoadingWheel from "../loading/LoadingWheel";
import ImageService from "@/services/ImageService.ts";
import useMaybeChangeUserRoleToDriver from "@/hooks/users/useMaybeChangeUserRoleToDriver";
import LoadingButton from "../buttons/LoadingButton";

const Navbar = () => {
  const { t } = useTranslation();
  const logout = useLogout();
  const isAuthenticated = useAuthentication();
  const location = useLocation();
  const navigate = useNavigate();
  const pathname = location?.pathname;
  const { isLoading, currentUser } = useCurrentUser();
  const isLoged = isAuthenticated;
  const [dropdownOpen, setDropdownOpen] = useState(false);

  const onSuccessOrIgnoreChangeRole = () => {
    if (pathname !== createTripsPath) {
      navigate(createTripsPath);
    }
  };

  const {
    maybeChangeUserRoleToDriver,
    isLoading: isMaybeChangeUserRoleLoading,
  } = useMaybeChangeUserRoleToDriver({
    onSuccess: onSuccessOrIgnoreChangeRole,
    onIgnore: onSuccessOrIgnoreChangeRole,
  });

  const toggleDropdown = () => setDropdownOpen(!dropdownOpen);

  return (
    <div className="nav-container">
      <div className="nav-sections-container">
        <div className="left-container">
          <div className="img-container">
            <Link to={homePath}>
              <img
                src={PoolitLogo}
                alt={t("poolit.logo")}
                className={styles.logoImg}
              />
            </Link>
          </div>
          {!isLoading && isLoged && (
            <>
              <div
                className={`nav-section-item ${
                  pathname === reservedTripsPath ? "active" : ""
                }`}
                key={reservedTripsPath}
              >
                <Link to={reservedTripsPath}>
                  <h4>{t("navbar.reserved")}</h4>
                </Link>
              </div>

              {currentUser &&
                (currentUser.role === UsersRoles.DRIVER ||
                  currentUser.role === UsersRoles.ADMIN) && (
                  <div
                    className={`nav-section-item ${
                      pathname === createdTripsPath ? "active" : ""
                    }`}
                    key={createdTripsPath}
                  >
                    <Link to={createdTripsPath}>
                      <h4>{t("navbar.created")}</h4>
                    </Link>
                  </div>
                )}

              {currentUser && currentUser.role === UsersRoles.ADMIN && (
                <div
                  className={`nav-section-item ${
                    pathname === adminPath ? "active" : ""
                  }`}
                  key={adminPath}
                >
                  <Link to={adminPath}>
                    <h4>{t("admin.title")}</h4>
                  </Link>
                </div>
              )}
            </>
          )}
        </div>
        {isLoged && !isLoading ? (
          <div className="right-container">
            <div className="create-trip-btn-container">
              <LoadingButton
                type="button"
                isLoading={isMaybeChangeUserRoleLoading}
                onClick={maybeChangeUserRoleToDriver}
                size="lg"
                className="secondary-btn create-trip-btn"
              >
                <i className="bi bi-plus-lg light-text h4"></i>
                <span className="button-text-style light-text h4">
                  {t("navbar.create")}
                </span>
              </LoadingButton>
            </div>

            <Dropdown show={dropdownOpen} onToggle={toggleDropdown} drop="down">
              <Dropdown.Toggle variant="link" id="profile-dropdown">
                <div className="img-profile-container">
                  {isLoading || currentUser === undefined ? (
                    <LoadingWheel
                      description={null}
                      iconClassName="h2 secondary-text"
                    />
                  ) : (
                    <CircleImg
                      src={ImageService.getSmallImageUrl(currentUser.imageUri)}
                      size={50}
                    />
                  )}
                </div>
              </Dropdown.Toggle>
              <Dropdown.Menu className={styles.menu_dropdown}>
                <Dropdown.Item as={Link} to={profilePath}>
                  {isLoading || currentUser === undefined ? (
                    <div className={styles.item_dropdown}>
                      <LoadingWheel
                        description={null}
                        iconClassName="h2 secondary-text"
                      />
                      <h3 className={styles.dropdown_text}>
                        {t("spinner.loading")}
                      </h3>
                    </div>
                  ) : (
                    <div className={styles.item_dropdown}>
                      <CircleImg
                        src={ImageService.getSmallImageUrl(
                          currentUser.imageUri
                        )}
                        size={28}
                      />
                      <h3 className={styles.dropdown_text}>
                        {t("format.name", {
                          name: currentUser.username,
                          surname: currentUser.surname,
                        })}
                      </h3>
                    </div>
                  )}
                </Dropdown.Item>
                <Dropdown.Item onClick={logout}>
                  <div className={styles.item_dropdown}>
                    <div className={styles.dropdown_text}>
                      <i className="bi bi-box-arrow-right light-text h5"></i>
                    </div>
                    <h5 className={styles.dropdown_text + " ms-2"}>
                      {t("navbar.log_out")}
                    </h5>
                  </div>
                </Dropdown.Item>
              </Dropdown.Menu>
            </Dropdown>
          </div>
        ) : (
          <div className="right-container">
            <Link to={registerPath}>
              <Button
                variant="primary"
                size="lg"
                active
                className="secondary-btn logout-btn"
              >
                <i className="bi bi-person-circle light-text h5"></i>
                <span className="button-text-style light-text h5">
                  {t("navbar.register")}
                </span>
              </Button>
            </Link>
            <Link to={loginPath}>
              <Button
                variant="primary"
                size="lg"
                active
                className="secondary-btn logout-btn"
              >
                <i className="bi bi-box-arrow-in-right h5"></i>
                <span className="button-text-style h5">
                  {t("navbar.log_in")}
                </span>
              </Button>
            </Link>
          </div>
        )}
      </div>
    </div>
  );
};

export default Navbar;

import styles from "./styles.module.scss";

interface FullReviewsProps<T> {
  title: string;
  empty_text: string;
  empty_icon: string;
  data: T[];
  component_name: React.FC<T>;
}

const FullReviews = <T,>({
  title,
  empty_text,
  empty_icon,
  data,
  component_name,
}: FullReviewsProps<T>) => {
  const generateItems = <T,>(data: T[], Component: React.FC<T>) => {
    return data.map((item, index) => <Component key={index} {...item} />);
  };

  const props = generateItems(data, component_name);

  return (
    <div className={styles.list_container}>
      <div className={styles.row_data}>
        <h2>{title}</h2>
      </div>
      {props && props.length > 0 ? (
        <div>
          <div>
            {props.map((item, index) => (
              <div className={styles.travel_info_list} key={index}>
                {item}
              </div>
            ))}
          </div>
        </div>
      ) : (
        <div className={styles.review_empty_container}>
          <i className={`bi-solid bi-${empty_icon} h2`}></i>
          <h3 className="italic-text placeholder-text">{empty_text}</h3>
        </div>
      )}
      <div></div>
    </div>
  );
};

export default FullReviews;

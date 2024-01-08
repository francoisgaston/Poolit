import FieldInterpolation, {
  FieldInterpolationValue,
} from "./FieldInterpolation";
import MaxValueFieldInterpolation from "./MaxValueFieldInterpolation";
import MinValueFieldInterpolation from "./MinValueFieldInterpolation";
import RegexFieldInterpolation from "./RegexFieldInterpolation";

interface IFieldInterpolationClass<V extends FieldInterpolationValue> {
  new (value: V): FieldInterpolation;
}

class FieldInterpolationsBuilder {
  private interpolations: Set<FieldInterpolation>;
  private builded: boolean;

  constructor() {
    this.interpolations = new Set<FieldInterpolation>();
    this.builded = false;
  }

  private setProperty<V extends FieldInterpolationValue>(
    FieldInterpolationClass: IFieldInterpolationClass<V>,
    value: V
  ): FieldInterpolationsBuilder {
    if (this.builded) {
      throw new Error("Interpolations already builded");
    }
    this.interpolations.add(new FieldInterpolationClass(value));
    return this;
  }

  public setMaxValue(value: number): FieldInterpolationsBuilder {
    return this.setProperty<number>(MaxValueFieldInterpolation, value);
  }

  public setMinValue(value: number): FieldInterpolationsBuilder {
    return this.setProperty<number>(MinValueFieldInterpolation, value);
  }

  public setRegexFormat(format: string): FieldInterpolationsBuilder {
    return this.setProperty<string>(RegexFieldInterpolation, format);
  }

  public build(): FieldInterpolation[] {
    this.builded = true;
    return [...this.interpolations];
  }
}

export default FieldInterpolationsBuilder;

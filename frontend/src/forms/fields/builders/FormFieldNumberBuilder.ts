import { ZodNumber, z } from "zod";
import FormFieldBuilder from "./FormFieldBuilder";
import MinValueFieldInterpolation from "../interpolations/MinValueFieldInterpolation";
import MaxValueFieldInterpolation from "../interpolations/MaxValueFieldInterpolation";

class FormFieldNumberBuilder extends FormFieldBuilder<ZodNumber> {
  private name: string;

  constructor(name: string) {
    super(z.number());
    this.name = name;
  }

  public isRequired(): FormFieldNumberBuilder {
    this.setSchema(this.schema.min, {
      value: 1,
      message: `error.${this.name}.required`,
    });
    return this;
  }

  public hasMinValue(minValue: number): FormFieldNumberBuilder {
    this.setSchema(this.schema.min, {
      value: minValue,
      message: `error.${this.name}.${MinValueFieldInterpolation.KEY}`,
    });
    this.setInterpolationsBuilder(
      this.interpolationsBuilder.setMinValue,
      minValue
    );
    return this;
  }

  public hasMaxValue(maxValue: number): FormFieldNumberBuilder {
    this.setSchema(this.schema.max, {
      value: maxValue,
      message: `error.${this.name}.${MaxValueFieldInterpolation.KEY}`,
    });
    this.setInterpolationsBuilder(
      this.interpolationsBuilder.setMaxValue,
      maxValue
    );
    return this;
  }
}

export default FormFieldNumberBuilder;

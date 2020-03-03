package fi.fmi.avi.model.impl;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

import fi.fmi.avi.model.NumericMeasure;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class NumericMeasureImpl implements NumericMeasure, Serializable {

    private static final long serialVersionUID = 8778896552671170215L;

    private Double value;
    private String uom;

    public NumericMeasureImpl() {
    }

    public NumericMeasureImpl(final Double value, final String uom) {
        this.value = value;
        this.uom = uom;
    }

    public NumericMeasureImpl(final Integer value, final String uom) {
        this(new Double(value), uom);
    }

    public NumericMeasureImpl(final NumericMeasure measure) {
        if (measure != null) {
            this.setValue(measure.getValue());
            this.setUom(measure.getUom());
        }
    }

    /* (non-Javadoc)
     * @see fi.fmi.avi.model.NumericMeasure#getValue()
     */
    @Override
    public Double getValue() {
        return value;
    }

    /* (non-Javadoc)
     * @see fi.fmi.avi.model.NumericMeasure#setValue(java.lang.Double)
     */
    @Override
    public void setValue(final Double value) {
        this.value = value;
    }

    /* (non-Javadoc)
     * @see fi.fmi.avi.model.NumericMeasure#getUom()
     */
    @Override
    public String getUom() {
        return uom;
    }

    /* (non-Javadoc)
     * @see fi.fmi.avi.model.NumericMeasure#setUom(java.lang.String)
     */
    @Override
    public void setUom(final String uom) {
        this.uom = uom;
    }

    public String toString() {
        return String.format("%.2f %s", this.value, this.uom);
    }
}
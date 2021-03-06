package fi.fmi.avi.model.immutable;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

import org.inferred.freebuilder.FreeBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fi.fmi.avi.model.MultiPolygonGeometry;

@FreeBuilder
@JsonDeserialize(builder = MultiPolygonGeometryImpl.Builder.class)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public abstract class MultiPolygonGeometryImpl implements MultiPolygonGeometry, Serializable {

    private static final long serialVersionUID = 4308464817438332280L;

    public static Builder builder() {
        return new Builder();
    }

    public static MultiPolygonGeometryImpl immutableCopyOf(final MultiPolygonGeometry polygon) {
        Objects.requireNonNull(polygon);
        if (polygon instanceof MultiPolygonGeometryImpl) {
            return (MultiPolygonGeometryImpl) polygon;
        } else {
            return Builder.from(polygon).build();
        }
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static Optional<MultiPolygonGeometryImpl> immutableCopyOf(final Optional<MultiPolygonGeometry> polygonsGeometry) {
        Objects.requireNonNull(polygonsGeometry);
        return polygonsGeometry.map(MultiPolygonGeometryImpl::immutableCopyOf);
    }

    public abstract Builder toBuilder();

    public static class Builder extends MultiPolygonGeometryImpl_Builder {

        @Deprecated
        public Builder() {
        }

        public static Builder from(final MultiPolygonGeometry value) {
            if (value instanceof MultiPolygonGeometryImpl) {
                return ((MultiPolygonGeometryImpl) value).toBuilder();
            } else {
                return MultiPolygonGeometryImpl.builder()//
                        .setSrsName(value.getSrsName())//
                        .setSrsDimension(value.getSrsDimension())//
                        .setAxisLabels(value.getAxisLabels())//
                        .addAllExteriorRingPositions(value.getExteriorRingPositions());

            }
        }

    }
}

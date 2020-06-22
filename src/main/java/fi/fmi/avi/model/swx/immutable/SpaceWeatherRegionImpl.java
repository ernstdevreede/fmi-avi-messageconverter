package fi.fmi.avi.model.swx.immutable;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

import org.inferred.freebuilder.FreeBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fi.fmi.avi.model.swx.AirspaceVolume;
import fi.fmi.avi.model.swx.SpaceWeatherRegion;

@FreeBuilder
@JsonDeserialize(builder = SpaceWeatherRegionImpl.Builder.class)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@JsonPropertyOrder({ "airspaceVolume", "locationIndicator" })
public abstract class SpaceWeatherRegionImpl implements SpaceWeatherRegion, Serializable {

    private static final long serialVersionUID = 207049872292188821L;

    public static Builder builder() {
        return new Builder();
    }

    public static SpaceWeatherRegionImpl immutableCopyOf(final SpaceWeatherRegion region) {
        Objects.requireNonNull(region);
        if (region instanceof SpaceWeatherRegionImpl) {
            return (SpaceWeatherRegionImpl) region;
        } else {
            return Builder.from(region).build();
        }
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static Optional<SpaceWeatherRegionImpl> immutableCopyOf(final Optional<SpaceWeatherRegion> region) {
        Objects.requireNonNull(region);
        return region.map(SpaceWeatherRegionImpl::immutableCopyOf);
    }

    public abstract Builder toBuilder();

    public static class Builder extends SpaceWeatherRegionImpl_Builder {
        @Deprecated
        Builder() {
        }

        public static Builder from(final SpaceWeatherRegion value) {
            if (value instanceof SpaceWeatherRegionImpl) {
                return ((SpaceWeatherRegionImpl) value).toBuilder();
            } else {
                return builder().setAirSpaceVolume(AirspaceVolumeImpl.immutableCopyOf(value.getAirSpaceVolume()))
                        .setLocationIndicator(value.getLocationIndicator());
            }
        }

        @Override
        @JsonDeserialize(as = AirspaceVolumeImpl.class)
        public Builder setAirSpaceVolume(final AirspaceVolume airSpaceVolume) {
            return super.setAirSpaceVolume(airSpaceVolume);
        }
    }
}

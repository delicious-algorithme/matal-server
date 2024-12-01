package matal.store.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QStore is a Querydsl query type for Store
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStore extends EntityPathBase<Store> {

    private static final long serialVersionUID = -524743609L;

    public static final QStore store = new QStore("store");

    public final StringPath address = createString("address");

    public final StringPath businessHours = createString("businessHours");

    public final StringPath category = createString("category");

    public final StringPath imageUrls = createString("imageUrls");

    public final BooleanPath isParking = createBoolean("isParking");

    public final BooleanPath isPetFriendly = createBoolean("isPetFriendly");

    public final BooleanPath isSoloDining = createBoolean("isSoloDining");

    public final BooleanPath isWaiting = createBoolean("isWaiting");

    public final StringPath keyword = createString("keyword");

    public final NumberPath<Double> latitude = createNumber("latitude", Double.class);

    public final NumberPath<Double> longitude = createNumber("longitude", Double.class);

    public final StringPath mainMenu = createString("mainMenu");

    public final StringPath name = createString("name");

    public final StringPath nearbyStation = createString("nearbyStation");

    public final StringPath negativeKeywords = createString("negativeKeywords");

    public final NumberPath<Double> negativeRatio = createNumber("negativeRatio", Double.class);

    public final NumberPath<Double> neutralRatio = createNumber("neutralRatio", Double.class);

    public final StringPath parkingTip = createString("parkingTip");

    public final StringPath phone = createString("phone");

    public final StringPath positiveKeywords = createString("positiveKeywords");

    public final NumberPath<Double> positiveRatio = createNumber("positiveRatio", Double.class);

    public final NumberPath<Double> rating = createNumber("rating", Double.class);

    public final StringPath recommendedMenu = createString("recommendedMenu");

    public final NumberPath<Long> reviewsCount = createNumber("reviewsCount", Long.class);

    public final StringPath reviewSummary = createString("reviewSummary");

    public final NumberPath<Long> storeId = createNumber("storeId", Long.class);

    public final StringPath storeLink = createString("storeLink");

    public final StringPath waitingTip = createString("waitingTip");

    public QStore(String variable) {
        super(Store.class, forVariable(variable));
    }

    public QStore(Path<? extends Store> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStore(PathMetadata metadata) {
        super(Store.class, metadata);
    }

}


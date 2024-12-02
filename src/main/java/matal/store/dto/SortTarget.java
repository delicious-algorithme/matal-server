package matal.store.dto;

public enum SortTarget {

    RATING("rating"),
    POSITIVE_RATIO("positiveRatio");

    private String name;

    SortTarget(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}

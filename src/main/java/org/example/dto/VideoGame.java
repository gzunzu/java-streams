package org.example.dto;

import org.apache.commons.collections4.CollectionUtils;
import org.example.utils.TextUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

public record VideoGame(
        String title,
        String developer,
        Set<String> platforms,
        Set<Genre> genres,
        int estimatedHours,
        LocalDate releaseDate,
        List<Nomination> nominations,
        boolean multiplayer) {

    @Override
    public String toString() {
        if (CollectionUtils.isNotEmpty(this.nominations)) {
            this.nominations.sort(new Nomination.NomitationComparator());
        }
        return "Title: " + this.title
                + "\n\tDeveloper: " + this.developer
                + "\n\tPlatforms: " + TextUtils.getAsPrettyString(this.platforms)
                + "\n\tGenres: " + TextUtils.getAsPrettyString(this.genres)
                + "\n\tEstimated length: " + this.estimatedHours + " hrs"
                + "\n\tRelease date: " + this.releaseDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                + "\n\tNominations: " + (CollectionUtils.isEmpty(this.nominations) ? "None" : TextUtils.getAsPrettyListString(this.nominations, 2))
                + "\n\tMultiplayer: " + (this.multiplayer ? "Yes" : "No")
                + "\n";
    }
}

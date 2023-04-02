package org.example;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Repository
public class VideoGameRepository {

    private final List<VideoGame> videoGames;

    public VideoGameRepository() {
        this.videoGames = new ArrayList<>();
    }

    public boolean add(final VideoGame... videoGames) {
        return this.add(List.of(videoGames));
    }

    public boolean add(final List<VideoGame> videoGames) {
        return this.videoGames.addAll(videoGames);
    }

    public List<VideoGame> getAll() {
        return this.videoGames;
    }

    public Optional<VideoGame> getByTitle(String title) {
        VideoGame result = null;
        for (VideoGame videoGame : this.videoGames) {
            if (title.equalsIgnoreCase(videoGame.title())) {
                result = videoGame;
                break;
            }
        }
        return Optional.ofNullable(result);
    }

    public List<VideoGame> getByDeveloper(final String developer) {
        final List<VideoGame> list = new ArrayList<>();
        for (VideoGame videoGame : this.videoGames) {
            if (developer.equalsIgnoreCase(videoGame.developer())) {
                list.add(videoGame);
            }
        }
        return list;
    }

    public List<VideoGame> getByGenreAndDeveloper(final Genre genre, final String developer) {
        final List<VideoGame> list = new ArrayList<>();
        for (VideoGame videoGame : this.videoGames) {
            if (developer.equalsIgnoreCase(videoGame.developer()) && videoGame.genres().contains(genre)) {
                list.add(videoGame);
            }
        }
        return list;
    }

    public List<VideoGame> getByGenre(final Genre genre) {
        final List<VideoGame> list = new ArrayList<>();
        for (VideoGame videoGame : this.videoGames) {
            if (videoGame.genres().contains(genre)) {
                list.add(videoGame);
            }
        }
        return list;
    }

    public Optional<Pair<Genre, Integer>> getFavouriteGenre() {
        final Map<Genre, Integer> count = new HashMap<>();
        for (VideoGame videoGame : this.videoGames) {
            for (Genre genre : videoGame.genres()) {
                if (count.containsKey(genre)) {
                    count.put(genre, count.get(genre) + 1);
                } else {
                    count.put(genre, 1);
                }
            }
        }
        int maxOccurrences = 0;
        Genre favGenre = null;
        for (Map.Entry<Genre, Integer> entry : count.entrySet()) {
            if (entry.getValue() > maxOccurrences) {
                maxOccurrences = entry.getValue();
                favGenre = entry.getKey();
            }
        }
        return Optional.of(Pair.of(favGenre, maxOccurrences));
    }

    public List<VideoGame> getByPlatform(final String platform) {
        final List<VideoGame> list = new ArrayList<>();
        for (VideoGame videoGame : this.videoGames) {
            if (videoGame.platforms().contains(platform)) {
                list.add(videoGame);
            }
        }
        return list;
    }

    public List<VideoGame> getByReleaseYear(final int year) {
        final List<VideoGame> list = new ArrayList<>();
        for (VideoGame videoGame : this.videoGames) {
            if (videoGame.releaseDate().getYear() == year) {
                list.add(videoGame);
            }
        }
        return list;
    }

    public List<VideoGame> getReleasedBeforeYear(final int year) {
        final List<VideoGame> list = new ArrayList<>();
        for (VideoGame videoGame : this.videoGames) {
            if (videoGame.releaseDate().getYear() < year) {
                list.add(videoGame);
            }
        }
        return list;
    }

    public List<VideoGame> getReleasedBeforeOrAfter(final int beforeYear, final int afterYear) {
        final List<VideoGame> list = new ArrayList<>();
        for (VideoGame videoGame : this.videoGames) {
            if (videoGame.releaseDate().getYear() < beforeYear || videoGame.releaseDate().getYear() >= afterYear) {
                list.add(videoGame);
            }
        }
        return list;
    }

    public Pair<Integer, Set<String>> getLessCommonPlatforms() {
        final Map<String, Integer> platformCount = new HashMap<>();
        int minOccurrences = -1;
        Set<String> platforms = new HashSet<>();

        for (VideoGame videoGame : this.videoGames) {
            for (String platform : videoGame.platforms()) {
                if (platformCount.containsKey(platform)) {
                    platformCount.put(platform, platformCount.get(platform) + 1);
                } else {
                    platformCount.put(platform, 1);
                }
            }
        }

        for (Map.Entry<String, Integer> entry : platformCount.entrySet()) {
            if (minOccurrences < 0) {
                minOccurrences = entry.getValue();
                platforms.add(entry.getKey());
            } else if (entry.getValue() == minOccurrences) {
                platforms.add(entry.getKey());
            } else if (entry.getValue() < minOccurrences) {
                minOccurrences = entry.getValue();
                platforms.clear();
                platforms.add(entry.getKey());
            }
        }
        return Pair.of(minOccurrences, platforms);
    }

    public Duration getAveragePlayingTime() {
        int gamesCount = 0;
        int totalHours = 0;
        for (VideoGame videoGame : this.videoGames) {
            gamesCount++;
            totalHours += videoGame.estimatedHours();
        }

        return Duration.ofMillis(TimeUnit.HOURS.toMillis(totalHours) / gamesCount);
    }

    public Optional<VideoGame> getShortestGame() {
        int minEstimatedHours = -1;
        VideoGame result = null;
        for (VideoGame videoGame : this.videoGames) {
            if (minEstimatedHours < 0 || videoGame.estimatedHours() < minEstimatedHours) {
                result = videoGame;
                minEstimatedHours = videoGame.estimatedHours();
            }
        }
        return Optional.ofNullable(result);
    }

    public Map<String, Integer> getMostNominatedGames(final int limit) {
        Map<String, Integer> nominated = new HashMap<>();
        List<Integer> topNominations = new ArrayList<>();
        Map<String, Integer> mostNominated = new HashMap<>();

        for (VideoGame videoGame : this.videoGames) {
            if (videoGame.nominations() != null) {
                nominated.put(videoGame.title(), videoGame.nominations().size());
                topNominations.add(videoGame.nominations().size());
            }
        }
        topNominations.sort(Comparator.naturalOrder());
        topNominations.sort(Comparator.reverseOrder());

        for (Map.Entry<String, Integer> entry : nominated.entrySet()) {
            for (int i = 0; i < limit; i++) {
                if (topNominations.get(i) != null && topNominations.get(i).equals(entry.getValue())) {
                    mostNominated.put(entry.getKey(), entry.getValue());
                }
            }
        }
        return mostNominated;
    }

    public Optional<Pair<String, Integer>> getMostAwardedGame() {
        int maxAwards = 0;
        VideoGame result = null;
        for (VideoGame videoGame : this.videoGames) {
            if (videoGame.nominations() != null) {
                int awardsCount = 0;
                for (Nomination nomination : videoGame.nominations()) {
                    if (nomination.won()) {
                        awardsCount++;
                    }
                }
                if (awardsCount > maxAwards) {
                    maxAwards = awardsCount;
                    result = videoGame;
                }
            }
        }
        return Optional.of(Pair.of(result.title(), maxAwards));
    }

    public Optional<Pair<String, Integer>> getMostAwardedGameByAwardLabel(final String awardLabel) {
        int maxAwards = 0;
        VideoGame result = null;
        for (VideoGame videoGame : this.videoGames) {
            if (videoGame.nominations() != null) {
                int awardsCount = 0;
                for (Nomination nomination : videoGame.nominations()) {
                    if (nomination.won() && awardLabel.equalsIgnoreCase(nomination.awards())) {
                        awardsCount++;
                    }
                }
                if (awardsCount > maxAwards) {
                    maxAwards = awardsCount;
                    result = videoGame;
                }
            }
        }
        return Optional.of(Pair.of(result.title(), maxAwards));
    }

    public Optional<VideoGame> getOldestMultiplayerToWinAnAward() {
        int minWinnerYear = -1;
        VideoGame result = null;

        for (VideoGame videoGame : this.videoGames) {
            if (videoGame.nominations() != null) {
                for (Nomination nomination : videoGame.nominations()) {
                    if (nomination.won() && (minWinnerYear < 0 || videoGame.releaseDate().getYear() < minWinnerYear) && videoGame.multiplayer()) {
                        minWinnerYear = videoGame.releaseDate().getYear();
                        result = videoGame;
                    }
                }
            }
        }
        return Optional.ofNullable(result);
    }

    public List<VideoGame> getMultiplayerGames() {
        final List<VideoGame> list = new ArrayList<>();
        for (VideoGame videoGame : this.videoGames) {
            if (videoGame.multiplayer()) {
                list.add(videoGame);
            }
        }
        return list;
    }
}

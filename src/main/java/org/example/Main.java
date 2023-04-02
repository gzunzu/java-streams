package org.example;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.example.utils.JsonHelper;
import org.example.utils.LoggerHelper;
import org.example.utils.TextUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Log4j2
public class Main {

    private static VideoGameRepository repository;

    public static void main(final String[] args) {

        repository = new VideoGameRepository();
        repository.add(JsonHelper.readJsonArrayFile("src/main/resources/videogames.json", VideoGame.class));

        printAllVideoGames();
        printMultiplayerGames();
        printVideoGameByTitle("Final Fantasy VII");
        printVideoGamesByGenre(Genre.HACK_AND_SLASH);
        printVideoGamesByGenreAndDeveloper(Genre.ROLE_PLAYING, "Square Enix");
        printFavouriteGenre();
        printVideoGamesByPlatform("Xbox One");
        printVideoGamesReleasedInYear(2017);
        printVideoGamesReleasedBeforeOrAfter(2000, 2018);
        printAveragePlayingTime();
        printShortestGame();
        printMostAwardedGame();
        printMostAwardedGameByAwardLabel("The game awards");
        printOldestMultiplayerGameToWinAnAward();
        printMostNominatedGames(5);
        printLessCommonPlatforms();

        LoggerHelper.shutDownLogs(Main.class);
    }

    private static void printAllVideoGames() {
        StringBuilder stringBuilder = new StringBuilder("These are all the available video games:")
                .append(System.lineSeparator())
                .append(TextUtils.getCollectionAsPrettyList(repository.getAll(), 0));
        log.info(stringBuilder.append(System.lineSeparator()));
    }

    private static void printVideoGameByTitle(final String title) {
        Optional<VideoGame> videoGame = repository.getByTitle(title);
        String introduction = (videoGame.isPresent() ? "Here's the video game " : "No video game found ") + "with title \"" + title + "\":";
        StringBuilder stringBuilder = new StringBuilder(introduction)
                .append(System.lineSeparator())
                .append(videoGame.isPresent() ? videoGame.get() : StringUtils.EMPTY);
        log.info(stringBuilder.append(System.lineSeparator()));
    }

    private static void printVideoGamesByGenreAndDeveloper(final Genre genre, final String developer) {
        List<String> result = new ArrayList<>();

        for (VideoGame videoGame : repository.getByGenreAndDeveloper(genre, developer)) {
            result.add(videoGame.title());
        }

        StringBuilder stringBuilder = new StringBuilder("These are all the video games of \"" + genre + "\" genre developed by \"" + developer + "\":")
                .append(System.lineSeparator())
                .append(TextUtils.getCollectionAsPrettyString(result));
        log.info(stringBuilder.append(System.lineSeparator()));
    }

    private static void printVideoGamesByGenre(final Genre genre) {
        List<String> result = new ArrayList<>();

        for (VideoGame videoGame : repository.getByGenre(genre)) {
            result.add(videoGame.title());
        }

        StringBuilder stringBuilder = new StringBuilder("These are all the \"" + genre + "\" genre video games:")
                .append(System.lineSeparator())
                .append(TextUtils.getCollectionAsPrettyString(result));
        log.info(stringBuilder.append(System.lineSeparator()));
    }

    private static void printFavouriteGenre() {
        Optional<Pair<Genre, Integer>> result = repository.getFavouriteGenre();
        if (result.isPresent()) {
            Pair<Genre, Integer> favouriteGenre = result.get();
            log.info("The most common genre is \"{}\" with {} game(s).{}",
                    favouriteGenre.getKey(),
                    favouriteGenre.getValue(),
                    System.lineSeparator());
        } else {
            log.info("No game found.{}", System.lineSeparator());
        }
    }

    private static void printVideoGamesByPlatform(final String platform) {
        List<String> result = new ArrayList<>();

        for (VideoGame videoGame : repository.getByPlatform(platform)) {
            result.add(videoGame.title());
        }

        StringBuilder stringBuilder = new StringBuilder("These are all the games available in \"" + platform + "\" platform:")
                .append(System.lineSeparator())
                .append(TextUtils.getCollectionAsPrettyString(result));
        log.info(stringBuilder.append(System.lineSeparator()));
    }

    private static void printLessCommonPlatforms() {
        Pair<Integer, Set<String>> result = repository.getLessCommonPlatforms();
        if (result != null && result.getKey() > 0L) {
            log.info("These are the less common platforms with just {} occurrence(s):{}{}{}",
                    result.getKey(),
                    System.lineSeparator(),
                    TextUtils.getCollectionAsPrettyString(result.getValue()),
                    System.lineSeparator());
        } else {
            log.info("No used platform found.{}", System.lineSeparator());
        }
    }

    private static void printVideoGamesReleasedInYear(final int year) {
        List<String> result = new ArrayList<>();

        for (VideoGame videoGame : repository.getByReleaseYear(year)) {
            result.add(videoGame.title());
        }

        StringBuilder stringBuilder = new StringBuilder("These are all the video games released in \"" + year + "\":")
                .append(System.lineSeparator())
                .append(TextUtils.getCollectionAsPrettyString(result));
        log.info(stringBuilder.append(System.lineSeparator()));
    }

    private static void printVideoGamesReleasedBeforeOrAfter(final int beforeYear, final int afterYear) {
        List<Pair<String, Integer>> result = new ArrayList<>();

        for (VideoGame videoGame : repository.getReleasedBeforeOrAfter(beforeYear, afterYear)) {
            result.add(Pair.of(videoGame.title(), videoGame.releaseDate().getYear()));
        }

        result.sort(Comparator.comparing(Pair::getValue));

        StringBuilder stringBuilder = new StringBuilder("These are all the video games released before \"" + beforeYear + "\" or after \"" + afterYear + "\":")
                .append(System.lineSeparator())
                .append(TextUtils.getCollectionAsPrettyString(result));
        log.info(stringBuilder.append(System.lineSeparator()));
    }

    private static void printAveragePlayingTime() {
        Duration duration = repository.getAveragePlayingTime();
        if (Duration.ZERO.equals(duration)) {
            log.info("No game or estimated playing time found.");
        } else {
            log.info("The average estimated playing time per game is {} HH:mm:ss.{}",
                    String.format("%02d:%02d:%02d", duration.toHours(), duration.toMinutesPart(), duration.toSecondsPart()),
                    System.lineSeparator());
        }
    }

    private static void printShortestGame() {
        Optional<VideoGame> result = repository.getShortestGame();
        if (result.isPresent()) {
            VideoGame shortestGame = result.get();
            log.info("The shortest game based on the estimated playing hours is \"{}\" with {} hrs.{}",
                    shortestGame.title(),
                    shortestGame.estimatedHours(),
                    System.lineSeparator());
        } else {
            log.info("No game found.{}", System.lineSeparator());
        }
    }

    private static void printMostNominatedGames(final int limit) {
        Map<String, Integer> result = repository.getMostNominatedGames(limit);
        List<Pair<String, Integer>> pairList = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : result.entrySet()) {
            pairList.add(Pair.of(entry.getKey(), entry.getValue()));
        }

        pairList.sort(Collections.reverseOrder(Comparator.comparing(Pair::getValue)));

        if (CollectionUtils.isNotEmpty(pairList)) {
            log.info("This is the top {} most nominated games:{}{}{}",
                    limit,
                    System.lineSeparator(),
                    TextUtils.getCollectionAsPrettyString(pairList),
                    System.lineSeparator());
        } else {
            log.info("No nominated game found.{}", System.lineSeparator());
        }
    }

    private static void printMostAwardedGame() {
        Optional<Pair<String, Integer>> result = repository.getMostAwardedGame();
        if (result.isPresent()) {
            Pair<String, Integer> mostAwardedGame = result.get();
            log.info("The most awarded game is \"{}\" with {} win(s).{}",
                    mostAwardedGame.getKey(),
                    mostAwardedGame.getValue(),
                    System.lineSeparator());
        } else {
            log.info("No awarded game found.{}", System.lineSeparator());
        }
    }

    private static void printMostAwardedGameByAwardLabel(final String awardLabel) {
        Optional<Pair<String, Integer>> result = repository.getMostAwardedGameByAwardLabel(awardLabel);
        if (result.isPresent()) {
            Pair<String, Integer> mostAwardedGame = result.get();
            log.info("The most awarded game by \"{}\" is \"{}\" with {} win(s).{}",
                    awardLabel,
                    mostAwardedGame.getKey(),
                    mostAwardedGame.getValue(),
                    System.lineSeparator());
        } else {
            log.info("No game awarded by {} found.{}", awardLabel, System.lineSeparator());
        }
    }

    private static void printOldestMultiplayerGameToWinAnAward() {
        Optional<VideoGame> videoGame = repository.getOldestMultiplayerToWinAnAward();
        if (videoGame.isPresent()) {
            log.info("The oldest multiplayer video game to win an award is \"{}\" ({}).{}",
                    videoGame.get().title(),
                    videoGame.get().releaseDate().getYear(),
                    System.lineSeparator()
            );
        } else {
            log.info("No multiplayer awarded game found.");
        }
    }

    private static void printMultiplayerGames() {
        List<String> result = new ArrayList<>();

        for (VideoGame videoGame : repository.getMultiplayerGames()) {
            result.add(videoGame.title());
        }

        StringBuilder stringBuilder = new StringBuilder("These are all the multiplayer titles:")
                .append(System.lineSeparator())
                .append(TextUtils.getCollectionAsPrettyString(result));
        log.info(stringBuilder.append(System.lineSeparator()));
    }
}
import java.util.*;
import java.io.*;

enum Outcome {
	WIN,
	LOSE,
	DRAW
}

interface Competitor<T extends Competitor<T>> {
	Outcome compete(T Competitor);
}

class Enums {
	private static Random rand = new Random(47);

	public static <T extends Enum<T>> T random(Class<T> ec) {
		return random(ec.getEnumConstants());
	}

	public static <T> T random(T[] values) {
		return values[rand.nextInt(values.length)];
	}
}

class RoShamBo {
	public static <T extends Competitor<T>> void match(T a, T b) {
		System.out.println(a + " vs. " + b + ": " + a.compete(b));
	}

	public static <T extends Enum<T> & Competitor<T>> void play(Class<T> rsbclass, int size) {
		for (int i = 0; i < size; i++) {
			match(Enums.random(rsbclass), Enums.random(rsbclass));
		}
	}
}

public enum Demo implements Competitor<Demo> {
	PAPER(Outcome.DRAW, Outcome.LOSE, Outcome.WIN),
	SCISSORS(Outcome.WIN, Outcome.DRAW, Outcome.LOSE),
	ROCK(Outcome.LOSE, Outcome.WIN, Outcome.DRAW);

	private Outcome vPaper;
	private Outcome vScissors;
	private Outcome vRock;

	Demo(Outcome paper, Outcome scissors, Outcome rock) {
		vPaper = paper;
		vScissors = scissors;
		vRock = rock;
	}

	public Outcome compete(Demo it) {
		switch (it) {
			default:
			case PAPER: return vPaper;
			case SCISSORS: return vScissors;
			case ROCK: return vRock;
		}
	}


	public static void main(String[] args) {
		RoShamBo.play(Demo.class, 10);
	}
}


/*
ROCK vs. ROCK: DRAW
SCISSORS vs. ROCK: LOSE
SCISSORS vs. ROCK: LOSE
SCISSORS vs. ROCK: LOSE
PAPER vs. SCISSORS: LOSE
PAPER vs. PAPER: DRAW
PAPER vs. SCISSORS: LOSE
ROCK vs. SCISSORS: WIN
SCISSORS vs. SCISSORS: DRAW
ROCK vs. SCISSORS: WIN
*/














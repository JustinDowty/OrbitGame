package game;

/**
 * The different blast types for ship.
 * @author Justin Dowty
 *
 */
public enum BlastTypes {
	/**
	 * Standard single blast.
	 */
	STANDARD,
	/**
	 * Two side diagonal blasts.
	 */
	SIDE_BLASTS,
	/**
	 * Side blasts with middle blast.
	 */
	MULTI_BLAST,
	/**
	 * Lazer blasts through aliens and meteors,
	 * does triple damage to bosses.
	 */
	LAZER
}

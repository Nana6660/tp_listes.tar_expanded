package fr.istic.prg1.list;

import fr.istic.prg1.list_util.SmallSet;
import fr.istic.prg1.list_util.SuperT;

/**
 * Classe représentant les sous-ensembles de la classe MySet.
 * 
 * @author Mickaël Foursov <foursov@univ-rennes1.fr>
 * @version 5.0
 * @since 2022-09-23
 */

public class SubSet implements SuperT {

	public final int rank;
	public final SmallSet set;

	public SubSet() {
		rank = 0;
		set = new SmallSet();
	}

	public SubSet(int rank, SmallSet set) {
		this.rank = rank;
		this.set = set;
	}

	@Override
	public SubSet copyOf() {
		return new SubSet(rank, set.copyOf());
	}

	@Override
	public String toString() {
		return "Subset [rank=" + rank + ", set=" + set + "]";
	}

	@Override
	public int hashCode() {
		// pour respecter les bonnes pratiques
		final int prime = 31;
		int result = 1;
		result = prime * result + rank;
		result = prime * result + ((set == null) ? 0 : set.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof SubSet)) {
			return false;
		}
		SubSet other = (SubSet) obj;
		return rank == other.rank && set.equals(other.set);
	}
}
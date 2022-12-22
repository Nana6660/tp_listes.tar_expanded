package fr.istic.prg1.list;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;


import fr.istic.prg1.list_util.Comparison;
import fr.istic.prg1.list_util.Iterator;
import fr.istic.prg1.list_util.List;
import fr.istic.prg1.list_util.SmallSet;

/**
 * @author MickaÃ«l Foursov <foursov@univ-rennes1.fr>
 * @version 5.0
 * @since 2022-09-23
 */

public class MySet extends List<SubSet> {

	/**
	 * Borne superieure pour les rangs des sous-ensembles.
	 */
	private static final int MAX_RANG = 128;
	private static final String NEW_VALUE = " nouveau contenu :";

	/**
	 * Sous-ensemble de rang maximal Ã  mettre dans le drapeau de la liste.
	 */
	private static final SubSet FLAG_VALUE = new SubSet(MAX_RANG, new SmallSet());
	/**
	 * EntrÃ©e standard.
	 */
	private static final Scanner standardInput = new Scanner(System.in);

	public MySet() {
		super();
		setFlag(FLAG_VALUE);
	}

	/**
	 * Fermer tout (actuellement juste l'entrÃ©e standard).
	 */
	public static void closeAll() {
		standardInput.close();
	}

	private static Comparison compare(int a, int b) {
		if (a < b) {
			return Comparison.INF;
		} else if (a == b) {
			return Comparison.EGAL;
		} else {
			return Comparison.SUP;
		}
	}

	public void print() {
		System.out.println(" [version corrigee de contenu]");
		this.print(System.out);
	}

	// //////////////////////////////////////////////////////////////////////////////
	// //////////// Appartenance, Ajout, Suppression, Cardinal
	// ////////////////////
	// //////////////////////////////////////////////////////////////////////////////

	/**
	 * @return true si le nombre saisi par l'utilisateur appartient Ã  this, false
	 *         sinon
	 */
	public boolean contains() {
		int value = readValue(standardInput, 0);
		System.out.println("Entrer le nombre Ã  vÃ©rifier compris entre 0 et 32767 : ");
		return this.contains(value);
	}

	/**
	 * @param element valeur Ã  tester
	 * @return true si valeur appartient Ã  l'ensemble, false sinon
	 */

	public boolean contains(int value) {
		int rankValue = value / 256;
		int setValue = value % 256;
		Iterator<SubSet> it = this.iterator();// list SubSet 
		while (!it.isOnFlag()) {
	        SubSet current = it.getValue();
			if (current.rank == rankValue) {
				return current.set.contains(setValue);
			}
			it.goForward();
		}
		return false;
	}

	/**
	 * Ajouter Ã  this toutes les valeurs saisis par l'utilisateur et afficher le
	 * nouveau contenu.
	 */
	public void add() {
		System.out.println(" valeurs a ajouter (-1 pour finir) : ");
		this.add(System.in);
		System.out.println(NEW_VALUE);
		this.printNewState();
	}

	/**
	 * Ajouter Ã  this toutes les valeurs prises dans is.
	 * 
	 * @param is flux d'entrÃ©e.
	 */
	public void add(InputStream is) {
		Scanner scanner = new Scanner(is);
		int value = scanner.nextInt();
		while (value != -1) {
			addNumber(value);
            value = scanner.nextInt();
		}
		scanner.close();
	}

	/**
	 * Ajouter element Ã  this,
	 *
	 * @param element valuer Ã  ajouter.
	 */
	public void addNumber(int value) {
		int rank = value / 256;
		int element = value % 256;
		Iterator<SubSet> it = this.iterator();
		SubSet current = it.getValue();
		while (compare(current.rank, rank) == Comparison.INF) {
			current = it.nextValue();
		}
		if (compare(current.rank, rank) == Comparison.EGAL) {
			current.set.add(element);
		} else if (compare(current.rank, rank) == Comparison.SUP) {
			SmallSet set = new SmallSet();
			set.add(element);
			it.addLeft(new SubSet(rank, set));
		}
	}

	/**
	 * Supprimer de this toutes les valeurs saisies par l'utilisateur et afficher le
	 * nouveau contenu.
	 */
	public void remove() {
		System.out.println("  valeurs a supprimer (-1 pour finir) : ");
		this.remove(System.in);
		System.out.println(NEW_VALUE);
		this.printNewState();
	}

	/**
	 * Supprimer de this toutes les valeurs prises dans is.
	 * 
	 * @param is flux d'entrÃ©e
	 */
	public void remove(InputStream is) {
		Scanner scanner = new Scanner(is);
		int value = scanner.nextInt();
		while (value != -1) {
			removeNumber(value);
			value = scanner.nextInt();
		}
		scanner.close();
	}

	/**
	 * Supprimer element de this.
	 * 
	 * @param element valeur Ã  supprimer
	 */
	public void removeNumber(int value) {
		int rank = value / 256;
		int element = value % 256;
		Iterator<SubSet> it = this.iterator();
		SubSet current = it.getValue();
		while (compare(current.rank, rank) == Comparison.INF) {
			current = it.nextValue();
		}
		if (compare(current.rank, rank) == Comparison.EGAL) {
			current.set.remove(element);
		}
		if (current.set.isEmpty()) {
			it.remove();
		}
	}

	/**
	 * @return taille de l'ensemble this
	 */
	public int size() {
		int size = 0;
		Iterator<SubSet> it = this.iterator();
		while (!it.isOnFlag()) {
			size += it.getValue().set.size();
			it.nextValue();
		}
		return size;
	}

	// /////////////////////////////////////////////////////////////////////////////
	// /////// Difference, DifferenceSymetrique, Intersection, Union ///////
	// /////////////////////////////////////////////////////////////////////////////

	/**
	 * This devient la diffÃ©rence de this et set2.
	 * 
	 * @param set2 deuxiÃ¨me ensemble
	 */
	public void difference(MySet set2) {
		if (!(this == set2)) {
			Iterator<SubSet> it1 = this.iterator();
			Iterator<SubSet> it2 = set2.iterator();
			while (!it2.isOnFlag()) {
				SubSet current1 = it1.getValue();
				SubSet current2 = it2.getValue();
				switch (compare(current1.rank, current2.rank)) {
				case INF:
					it1.goForward();
					break;
				case SUP:
					it2.goForward();
					break;
				default:
					current1.set.difference(current2.set);
					if (current1.set.isEmpty()) {
						it1.remove();
					} else {
						it1.goForward();
					}
				it2.goForward();
				}
		    }
		} else {
			this.clear();
		}
	}

	/**
	 * This devient la diffÃ©rence symÃ©trique de this et set2.
	 * 
	 * @param set2 deuxiÃ¨me ensemble
	 */
	public void symmetricDifference(MySet set2) {
		if (!(this == set2)) {
			Iterator<SubSet> it1 = this.iterator();
			Iterator<SubSet> it2 = set2.iterator();
			while (!it1.isOnFlag()) {
				SubSet current1 = it1.getValue();
				SubSet current2 = it2.getValue();
				switch (compare(current1.rank, current2.rank)) {
					case INF:
						it1.goForward();
						break;
					case SUP:
						it1.addLeft(current2.copyOf());
						it1.goForward();
						it2.goForward();
						break;
					default:
						current1.set.symmetricDifference(current2.set);
						if (current1.set.isEmpty()) {
							it1.goForward(); 
					} else {
						it2.goForward();
					}
					it2.goForward();
				}
			}
			while (!it2.isOnFlag()) {
				it1.addLeft(it2.getValue());
				it2.goForward();
			}
		} else {
			this.clear();
		}
	}

	/**
	 * This devient l'intersection de this et set2.
	 * 
	 * @param set2 deuxiÃ¨me ensemble
	 */
	public void intersection(MySet set2) {
		if (!(this == set2)) {
			Iterator<SubSet> it1 = this.iterator();
			Iterator<SubSet> it2 = set2.iterator();
			while (!it1.isOnFlag()) {
				SubSet current1 = it1.getValue();
				SubSet current2 = it2.getValue();
				switch (compare(current1.rank, current2.rank)) {
				case INF:
					it1.remove();
					break;
				case SUP:
					it2.goForward();
					break;
				default:
					current1.set.intersection(current2.set);
					if (current1.set.isEmpty()) {
						it1.remove();
					} else {
						it1.goForward();
					}
					it2.goForward();
				}
			}
		}
	}

	/**
	 * This devient l'union de this et set2.
	 * 
	 * @param set2 deuxiÃ¨me ensemble
	 */
	public void union(MySet set2) {
		if (!(this == set2)) {
			Iterator<SubSet> it1 = this.iterator();
			Iterator<SubSet> it2 = set2.iterator();
			while (!it1.isOnFlag()) {
				SubSet current1 = it1.getValue();
				SubSet current2 = it2.getValue();
				switch (compare(current1.rank, current2.rank)) {
					case INF:
						it1.goForward();
						break;
					case SUP:
						it1.addLeft(current2.copyOf());
						it1.goForward();
						it2.goForward();
						break;
					default:
						current1.set.union(current2.set.copyOf());
						it1.goForward();
						it2.goForward();
				}
			}
			while(!it2.isOnFlag()) {
				this.addTail(it2.getValue().copyOf());
				it2.goForward();
			}
		}
	}

	// /////////////////////////////////////////////////////////////////////////////
	// /////////////////// Egalite, Inclusion ////////////////////
	// /////////////////////////////////////////////////////////////////////////////

	/**
	 * @param o deuxiÃ¨me ensemble
	 * 
	 * @return true si les ensembles this et o sont Ã©gaux, false sinon
	 */
	@Override
	public boolean equals(Object o) {
		boolean b = true;
		if (this == o) {
			b = true;
		} else if (!(o instanceof MySet)) {
			b = false;
		} else {
			Iterator<SubSet> it1 = this.iterator();
			Iterator<SubSet> it2 = ((MySet) o).iterator();
			while (!(it1.isOnFlag() && it2.isOnFlag()) && b) {
				SubSet current1 = it1.getValue();
				SubSet current2 = it2.getValue();
				switch(compare(current1.rank, current2.rank)) {
				case INF:
					b = false;
					break;
				case SUP:
					b = false;
					break;
				default:
					if(!current1.set.equals(current2.set)) {
						b = false;
					}
					it1.goForward();
					it2.goForward();
				}
			}
		}
		return b;
	}

	/**
	 * @param set2 deuxiÃ¨me ensemble
	 * @return true si this est inclus dans set2, false sinon
	 */
	public boolean isIncludedIn(MySet set2) {
		boolean bool = true;
		if (!(this == set2)) {
			Iterator<SubSet> it1 = this.iterator();
			Iterator<SubSet> it2 = set2.iterator();
			while (!it1.isOnFlag() && bool) {
				SubSet current1 = it1.getValue();
				SubSet current2 = it2.getValue();
				switch (compare(current1.rank, current2.rank)) {
				case INF:
					bool = false;
					break;
				case SUP:
					it2.goForward();
					break;
				default:
					if (!current1.set.isIncludedIn(current2.set)) {
						bool = false;
					}
					it1.goForward();
					it2.goForward();
				}
			}
		} else {
			return true;
		}
		return bool;
	}

	// /////////////////////////////////////////////////////////////////////////////
	// //////// Rangs, Restauration, Sauvegarde, Affichage //////////////
	// /////////////////////////////////////////////////////////////////////////////

	/**
	 * Afficher les rangs prÃ©sents dans this.
	 */
	public void printRanks() {
		System.out.println(" [version corrigee de rangs]");
		this.printRanksAux();
	}

	private void printRanksAux() {
		int count = 0;
		Iterator<SubSet> it = this.iterator();
		StringBuilder line = new StringBuilder("Rangs presents : ");
		while (!it.isOnFlag()) {
			line.append(it.getValue().rank + "  ");
			count = count + 1;
			if (count == 10) {
				line.append("\n");
				count = 0;
			}
			it.goForward();

		}
		System.out.println(line.toString());
		if (count > 0) {
			System.out.println("\n");
		}
	}

	/**
	 * CrÃ©er this Ã  partir dâ€™un fichier choisi par lâ€™utilisateur contenant une
	 * sÃ©quence dâ€™entiers positifs terminÃ©e par -1 (cf f0.ens, f1.ens, f2.ens,
	 * f3.ens et f4.ens).
	 */
	public void restore() {
		String fileName = readFileName();
		InputStream inFile;
		try {
			inFile = new FileInputStream(fileName);
			System.out.println(" [version corrigee de restauration]");
			this.clear();
			this.add(inFile);
			inFile.close();
			System.out.println(NEW_VALUE);
			this.printNewState();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("fichier " + fileName + " inexistant");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("probleme de fermeture du fichier " + fileName);
		}
	}

	/**
	 * Sauvegarder this dans un fichier dâ€™entiers positifs terminÃ© par -1.
	 */
	public void save() {
		System.out.println(" [version corrigee de sauvegarde]");
		OutputStream outFile;
		try {
			outFile = new FileOutputStream(readFileName());
			this.print(outFile);
			outFile.write("-1\n".getBytes());
			outFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("pb ouverture fichier lors de la sauvegarde");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("probleme de fermeture du fichier");
		}
	}

	/**
	 * @return l'ensemble this sous forme de chaÃ®ne de caractÃ¨res.
	 */
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		int count = 0;
		SubSet subSet;
		int startValue;
		Iterator<SubSet> it = this.iterator();
		while (!it.isOnFlag()) {
			subSet = it.getValue();
			startValue = subSet.rank * 256;
			for (int i = 0; i < 256; ++i) {
				if (subSet.set.contains(i)) {
					StringBuilder number = new StringBuilder(String.valueOf(startValue + i));
					int numberLength = number.length();
					for (int j = 6; j > numberLength; --j) {
						number.append(" ");
					}
					result.append(number);
					++count;
					if (count == 10) {
						result.append("\n");
						count = 0;
					}
				}
			}
			it.goForward();
		}
		if (count > 0) {
			result.append("\n");
		}
		return result.toString();
	}

	/**
	 * Imprimer this dans outFile.
	 *
	 * @param outFile flux de sortie
	 */
	private void print(OutputStream outFile) {
		try {
			String string = this.toString();
			outFile.write(string.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Afficher l'ensemble avec sa taille et les rangs prÃ©sents.
	 */
	private void printNewState() {
		this.print(System.out);
		int size = this.size();
		System.out.println("Nombre d'elements : " + size);
		this.printRanksAux();
	}

	/**
	 * @param scanner
	 * @param min     valeur minimale possible
	 * @return l'entier lu au clavier (doit Ãªtre entre min et 32767)
	 */
	private static int readValue(Scanner scanner, int min) {
		int value = scanner.nextInt();
		while (value < min || value > 32767) {
			System.out.println("valeur incorrecte");
			value = scanner.nextInt();
		}
		return value;
	}

	/**
	 * @return nom de fichier saisi psar l'utilisateur
	 */
	private static String readFileName() {
		System.out.println(" nom du fichier : ");
		return standardInput.next();
	}
}
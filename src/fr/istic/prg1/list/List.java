package fr.istic.prg1.list;

import fr.istic.prg1.list_util.Iterator;
import fr.istic.prg1.list_util.SuperT;

public class List<T extends SuperT> {
	// liste en double chainage par references

	private class Element {
		// element de List<Item> : (Item, Element, Element)
		public T value;
		public Element left, right;

		public Element() {
			value = null;
			left = null;
			right = null;
		}
	} // class Element

	public class ListIterator implements Iterator<T> {
		private Element current;

		private ListIterator() {
			current = List.this.flag.right;

		}

		@Override
		public void goForward() {
			current = this.current.right;

		}

		@Override
		public void goBackward() {
			current = this.current.left;

		}

		@Override
		public void restart() {
			current = List.this.flag.right;

		}

		@Override
		public boolean isOnFlag() {
			boolean temp = false ; 
			if(current.equals(flag)) {
				temp = true;
			}return temp ; 
		}

		@Override
		public void remove() {
			try {
				assert current != flag : "\n\n\nimpossible de retirer le drapeau\n\n\n";
				Element elementL = current.left;
				Element elementR = current.right;
				elementL.right = elementR;
				elementR.left = elementL;
				current = current.right;
			} catch (AssertionError e) {
				e.printStackTrace();
				System.exit(0);
			}

		}

		@Override
		public T getValue() {
			return current.value;
		}

		@Override
		public T nextValue() {
			current = this.current.right;
			return current.value;
		}

		@Override
		public void addLeft(T v) {
			Element elemnt = new Element();
			elemnt.value = v;
			elemnt.right = current;
			elemnt.left = current.left;
			current.left.right = elemnt;
			current.left = elemnt;
			current = elemnt;
		}

		@Override
		public void addRight(T v) {
			Element elemnt = new Element();
			elemnt.value = v;
			elemnt.right = current.right;
			elemnt.left = current;
			current.right.left = elemnt;
			current.right = elemnt;
			current = elemnt;
		}

		@Override
		public void setValue(T v) {
			this.current.value = v;
		}

		@Override
		public String toString() {
			return "parcours de liste : pas d'affichage possible \n";
		}

		@Override
		public void selfDestroy() {
		}

	} // class IterateurListe

	private Element flag;

	public List() {
		flag = new Element();
		flag.left = this.flag;
		flag.right = this.flag;
	}

	public ListIterator iterator() {
		return new ListIterator();
	}

	public boolean isEmpty() {
		boolean temp = false ; 
		ListIterator itList = this.iterator();
		if(itList.isOnFlag()) {
			temp = true;
		}
		return temp ; 
	}

	public void clear() {
		flag.right = this.flag;
		flag.left = this.flag;
	}

	public void setFlag(T v) {
		this.flag.value = v;
	}

	public void addHead(T v) {
		ListIterator it = this.iterator();
		it.addRight(v);

	}

	public void addTail(T v) {
		ListIterator it = this.iterator();
		while (!it.isOnFlag()) {
			it.goForward();
		}
		it.addLeft(v);
	}

	@SuppressWarnings("unchecked")
	public List<T> clone() {
		List<T> nouvListe = new List<T>();
		ListIterator p = iterator();
		while (!p.isOnFlag()) {
			nouvListe.addTail((T) p.getValue().copyOf());
			// UNE COPIE EST NECESSAIRE !!!
			p.goForward();
		}
		return nouvListe;
	}

	@Override
	public String toString() {
		String s = "contenu de la liste : \n";
		ListIterator p = iterator();
		while (!p.isOnFlag()) {
			s = s + p.getValue().toString() + " ";
			p.goForward();
		}
		return s ;
	}
}

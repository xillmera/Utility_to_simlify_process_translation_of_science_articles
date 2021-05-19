package sciencearticleparser;

import java.util.*;

public class DataFrag {
	// класс фрагмента данных.

	// обозначение и нумерация фрагмента в рабочем файле
	private final String fragSep = "Frag_";
	private int fragNum; // номер фрагмента передается следующему фрагменту.

	// переменная для хранения текста разбитого на предложения Текста
	// желательно номер предложения хранить в одной графе с ним
	private List<String> separateText = new ArrayList<String>();
	private int firstSentNum; // номер первого предложения для нумерации предложений в тексте
	private int nextSentNum=0; // номер последнего предложения для передачи следующему фрагменту

	public DataFrag (int fragNum , int firstSentNum){
		this.fragNum = fragNum;
		this.firstSentNum = firstSentNum;
	}

	public boolean wasUsed (){
		return nextSentNum>0 ? true : false;
	}

	private String bufSentense = "";
	public void addSubSentense (String subSentense){
		// добавляет предложение в конец текущего определяемого предложения
		bufSentense += subSentense+" ";
		nextSentNum++;
	}

	public void addSentense (){ // команда на завершение предложения текущего
		// добавляет законченное предложение в буфере в конечное местоположение и очищает буфер
		// желательно номер добавить тут
		if (bufSentense.equals("")) return;
		separateText.add(bufSentense);
		bufSentense = "";
        }

        private enum WorkspaceType {ForTest,ForTranslate}
	private String workspaceForming (String sentense, Integer sentNum,WorkspaceType type){
		// метод добавляет рабочую область предложению.
		String workSpace = "";
		switch(type){
			case ForTest:
				workSpace =
					"\t"+sentNum+". "+sentense+"\n";
				break;
			case ForTranslate:
				workSpace =
					"\t"+sentNum+". "+sentense+"\n"+
						"\t\t"+"[st^ : ]>[end^ : ]\n"+
						"\t\t"+"Незнакомые слова (перевод) : \n\t\t\t\n"+
						"\t\t"+"Термины (определение, ан+рус) : \n\t\t\t\n"+
						"\t\t"+"Перевод фрагмента : \n"+
						"\t\t\t"+"ИСХ > "+sentense+"\n"+
						"\t\t\t"+"МАШ > \n"+
						"\t\t\t"+"КОН > \n";
				break;
		}
		return workSpace;
	}

	@Override
	public String toString (){
		// метод соединяет все предложения в одину строку для печати
		// при этом каждому предложению формирует рабочую область
		String outPutLine = fragSep+fragNum+"\n";
		int sentNum = firstSentNum;

		// вариант добавления номера сразу на продакшене
		for ( String sent : separateText ){
			//подготовка строки
			//формирование рабочей области
			String localLine = workspaceForming(sent, sentNum,WorkspaceType.ForTranslate);
			//склеивание воедино всех строк
			outPutLine += localLine;
			sentNum++;
		}
		return  outPutLine;
        }
}

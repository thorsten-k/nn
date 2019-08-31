package de.kisner.nn.tr;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.openfuxml.content.table.Body;
import org.openfuxml.content.table.Content;
import org.openfuxml.content.table.Row;
import org.openfuxml.content.table.Table;
import org.openfuxml.exception.OfxAuthoringException;
import org.openfuxml.factory.xml.ofx.content.text.XmlTitleFactory;
import org.openfuxml.factory.xml.table.XmlCellFactory;
import org.openfuxml.factory.xml.table.XmlHeadFactory;
import org.openfuxml.factory.xml.table.XmlTableFactory;
import org.openfuxml.renderer.text.OfxTextRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tipprunde.model.xml.liga.Match;
import org.tipprunde.model.xml.liga.Round;

import net.sf.exlp.util.xml.JaxbUtil;

public class OfxVectorTable
{
	final static Logger logger = LoggerFactory.getLogger(OfxVectorTable.class);

	private final Table table;
	private final List<String> vBalance;
	
	public OfxVectorTable(Round round)
	{
		table = XmlTableFactory.build();
		table.setTitle(XmlTitleFactory.build(round.getName()));
		Content content = new Content();
		content.setHead(XmlHeadFactory.build(XmlCellFactory.createParagraphCell("Heim"),XmlCellFactory.createParagraphCell("Gast")));
		
		Body body = new Body();
		for(Match match : round.getMatch())
		{
			Row row = new Row();
			row.getCell().add(XmlCellFactory.createParagraphCell(match.getLeft().getOpponent().getName()));
			row.getCell().add(XmlCellFactory.createParagraphCell(match.getRight().getOpponent().getName()));
			body.getRow().add(row);
		}
		content.getBody().add(body);
		
		table.setContent(content);
		
		vBalance = new ArrayList<>();
	}
	
	public void debug()
	{
		try
		{
			JaxbUtil.trace(table);
			OfxTextRenderer.table(table,System.out);
		}
		catch (OfxAuthoringException | IOException e) {e.printStackTrace();}
	}
	
	public void addData(double d)
	{
		BigDecimal bd = BigDecimal.valueOf(d);
		bd = bd.setScale(2, RoundingMode.HALF_UP);
		
		vBalance.add(bd.toPlainString());
	}
	
	public void addData(String s)
	{		
		vBalance.add(s);
	}
	
	public void addVector(String text)
	{
		table.getContent().getHead().getRow().get(0).getCell().add(XmlCellFactory.createParagraphCell(text));
		for(int i=0;i<vBalance.size();i++)
		{
			table.getContent().getBody().get(0).getRow().get(i).getCell().add(XmlCellFactory.createParagraphCell(vBalance.get(i)));
		}
		vBalance.clear();
	}
}
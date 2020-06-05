package com.xw.poseidon.utils.mybatis;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.OutputUtilities;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.ListUtilities;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;

public class PoseidonPluginAdapter extends PluginAdapter {
	
	@Override
	public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		
		this.addMethodInsertBatch(interfaze, introspectedTable);
		this.addMethodSelectByCondition(interfaze, introspectedTable);
		this.addMethodSelectPageByCondition(interfaze, introspectedTable);
		this.addMethodSelectPageByConditionCount(interfaze, introspectedTable);
		
        // 添加相应的包
        interfaze.addImportedType(new FullyQualifiedJavaType(("org.apache.ibatis.annotations.Param")));
        interfaze.addImportedType(new FullyQualifiedJavaType("java.util.List"));
        
        return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
	}
	
	protected void addMethodInsertBatch(Interface interfaze,IntrospectedTable introspectedTable) {
		// 生成方法
        Method method = new Method("insertBatch");
        // 设置方法类型
        method.setVisibility(JavaVisibility.PUBLIC);

        // 设置方法返回值类型
        method.setReturnType(new FullyQualifiedJavaType("Integer"));

        // 设置方法参数
        method.addParameter(new Parameter(new FullyQualifiedJavaType("List<" + introspectedTable.getTableConfiguration().getDomainObjectName() + ">"), "list"));
        
        interfaze.addMethod(method);
	}
	
	protected void addMethodSelectByCondition(Interface interfaze,IntrospectedTable introspectedTable) {
		// 生成方法
        Method method = new Method("selectByCondition");
        // 设置方法类型
        method.setVisibility(JavaVisibility.PUBLIC);

        // 设置方法返回值类型
        FullyQualifiedJavaType returnType = new FullyQualifiedJavaType("List<" + introspectedTable.getTableConfiguration().getDomainObjectName() + ">");
        method.setReturnType(returnType);

        // 设置方法参数
        method.addParameter(new Parameter(new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()), "record"));
        
        interfaze.addMethod(method);
	}
	
	protected void addMethodSelectPageByCondition(Interface interfaze,IntrospectedTable introspectedTable) {
		// 生成方法
        Method method = new Method("selectPageByCondition");
        // 设置方法类型
        method.setVisibility(JavaVisibility.PUBLIC);

        // 设置方法返回值类型
        FullyQualifiedJavaType returnType = new FullyQualifiedJavaType("List<" + introspectedTable.getTableConfiguration().getDomainObjectName() + ">");
        method.setReturnType(returnType);

        // 设置方法参数
        FullyQualifiedJavaType integerJavaType = new FullyQualifiedJavaType("Integer");
        
        Parameter userParameter = new Parameter(new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()), "record");
        userParameter.addAnnotation("@Param(\"record\")");
        method.addParameter(0, userParameter);
        
        Parameter pageStartParameter = new Parameter(integerJavaType, "pageStart");
        pageStartParameter.addAnnotation("@Param(\"pageStart\")");
        method.addParameter(1, pageStartParameter);

        Parameter pageSizeParameter = new Parameter(integerJavaType, "pageSize");
        pageSizeParameter.addAnnotation("@Param(\"pageSize\")");
        method.addParameter(2, pageSizeParameter);
        
        interfaze.addMethod(method);
	}
	
	protected void addMethodSelectPageByConditionCount(Interface interfaze,IntrospectedTable introspectedTable) {
		// 生成方法
        Method method = new Method("selectPageByConditionCount");
        // 设置方法类型
        method.setVisibility(JavaVisibility.PUBLIC);

        // 设置方法返回值类型
        method.setReturnType(new FullyQualifiedJavaType("Integer"));

        // 设置方法参数
        method.addParameter(new Parameter(new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()), "record"));
        
        interfaze.addMethod(method);
	}

	@Override
	public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
		XmlElement parentElement = document.getRootElement();
		
		// 查询条件 base_condition
		addBaseConditionElement(parentElement, introspectedTable);
		
		// 查询条件 base_condition_page
		addBaseConditionPageElement(parentElement, introspectedTable);
		
		// 批量现在insertBatch
		addInsertBatchElement(parentElement, introspectedTable);
		
		// 条件查询 selectByCondition
		addSelectByConditionElement(parentElement, introspectedTable);
		
		// 分页查询 selectPageByCondition
		addSelectPageByConditionElement(parentElement, introspectedTable);
		
		// 查询总数据量 selectPageCountByCondition
		addSelectPageByConditionCountElement(parentElement, introspectedTable);
		
		return super.sqlMapDocumentGenerated(document, introspectedTable);
	}
	
	protected void addInsertBatchElement(XmlElement parentElement, IntrospectedTable introspectedTable) {
		XmlElement batchInser = new XmlElement("insert");
		batchInser.addAttribute(new Attribute("id","insertBatch"));
		batchInser.addAttribute(new Attribute("parameterType","List"));
		
		StringBuilder insertClause = new StringBuilder();
		StringBuilder valuesClause = new StringBuilder();
		insertClause.append("insert into "); //$NON-NLS-1$
		insertClause.append(introspectedTable
				.getFullyQualifiedTableNameAtRuntime());
		insertClause.append(" ("); //$NON-NLS-1$
		valuesClause.append(" ("); //$NON-NLS-1$
		
		
		List<String> valuesClauses = new ArrayList<String>();
		List<IntrospectedColumn> columns = ListUtilities.removeIdentityAndGeneratedAlwaysColumns(introspectedTable.getAllColumns());
		for (int i = 0; i < columns.size(); i++) {
			IntrospectedColumn introspectedColumn = columns.get(i);
			insertClause.append(MyBatis3FormattingUtilities
					.getEscapedColumnName(introspectedColumn));
//					valuesClause.append(MyBatis3FormattingUtilities
//							.getParameterClause(introspectedColumn));
			//这里吧#{id,jdbcType=BIGINT}加工成#{item.id,jdbcType=BIGINT}
			String parameterClause = MyBatis3FormattingUtilities.getParameterClause(introspectedColumn);
			if(parameterClause.indexOf('{')>0){
				StringBuilder sbs = new StringBuilder();
				sbs.append(parameterClause.substring(0,2))
						.append("item.").append(parameterClause.substring(2));
				parameterClause= sbs.toString();
			}
			valuesClause.append(parameterClause);
			if (i + 1 < columns.size()) {
				insertClause.append(", "); //$NON-NLS-1$
				valuesClause.append(", "); //$NON-NLS-1$
			}
			if (valuesClause.length() > 80) {
				batchInser.addElement(new TextElement(insertClause.toString()));
				insertClause.setLength(0);
				OutputUtilities.xmlIndent(insertClause, 1);
				
				valuesClauses.add(valuesClause.toString());
				valuesClause.setLength(0);
				OutputUtilities.xmlIndent(valuesClause, 1);
			}
		}
		insertClause.append(") values");
		batchInser.addElement(new TextElement(insertClause.toString()));
		valuesClause.append(')');
		valuesClauses.add(valuesClause.toString());
		XmlElement foreach = new XmlElement("foreach");
		foreach.addAttribute(new Attribute("collection","list"));
		foreach.addAttribute(new Attribute("item","item"));
		foreach.addAttribute(new Attribute("separator",","));
		for (String clause : valuesClauses) {
			foreach.addElement(new TextElement(clause));
		}
		batchInser.addElement(foreach);
		
        parentElement.addElement(batchInser);
	}
	
	protected void addBaseConditionElement(XmlElement parentElement,IntrospectedTable introspectedTable) {
		// 增加base_condition
		XmlElement sql_base_condition = new XmlElement("sql");
		sql_base_condition.addAttribute(new Attribute("id", "base_condition"));
		//在这里添加where条件
        XmlElement selectTrimElement = new XmlElement("trim"); //设置trim标签
        selectTrimElement.addAttribute(new Attribute("prefix", "WHERE"));
        selectTrimElement.addAttribute(new Attribute("prefixOverrides", "AND | OR")); //添加where和and
		StringBuilder sb = new StringBuilder();
		for(IntrospectedColumn introspectedColumn : introspectedTable.getAllColumns()) {
			XmlElement selectNotNullElement = new XmlElement("if"); //$NON-NLS-1$
			sb.setLength(0);
			sb.append("null != ");
			sb.append(introspectedColumn.getJavaProperty());
			selectNotNullElement.addAttribute(new Attribute("test", sb.toString()));
			sb.setLength(0);
			// 添加and
			sb.append(" and ");
			// 添加别名t
			sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
			// 添加等号
			sb.append(" = ");
			sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
            selectNotNullElement.addElement(new TextElement(sb.toString()));
            selectTrimElement.addElement(selectNotNullElement);
		}
		sql_base_condition.addElement(selectTrimElement);
		parentElement.addElement(sql_base_condition);
	}
	
	protected void addBaseConditionPageElement(XmlElement parentElement,IntrospectedTable introspectedTable) {
		// 增加base_condition
		XmlElement sql_base_condition = new XmlElement("sql");
		sql_base_condition.addAttribute(new Attribute("id", "base_condition_page"));
		//在这里添加where条件
        XmlElement selectTrimElement = new XmlElement("trim"); //设置trim标签
        selectTrimElement.addAttribute(new Attribute("prefix", "WHERE"));
        selectTrimElement.addAttribute(new Attribute("prefixOverrides", "AND | OR")); //添加where和and
		StringBuilder sb = new StringBuilder();
		for(IntrospectedColumn introspectedColumn : introspectedTable.getAllColumns()) {
			XmlElement selectNotNullElement = new XmlElement("if"); //$NON-NLS-1$
			sb.setLength(0);
			sb.append("null != record.");
			sb.append(introspectedColumn.getJavaProperty());
			selectNotNullElement.addAttribute(new Attribute("test", sb.toString()));
			sb.setLength(0);
			// 添加and
			sb.append(" and ");
			// 添加别名t
			sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
			// 添加等号
			sb.append(" = ");
			sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn,"record."));
            selectNotNullElement.addElement(new TextElement(sb.toString()));
            selectTrimElement.addElement(selectNotNullElement);
		}
		sql_base_condition.addElement(selectTrimElement);
		parentElement.addElement(sql_base_condition);
	}
	
	protected void addSelectByConditionElement(XmlElement parentElement,IntrospectedTable introspectedTable) {
        XmlElement answer = new XmlElement("select"); //$NON-NLS-1$

        answer.addAttribute(new Attribute(
                "id", "selectByCondition")); //$NON-NLS-1$
        if (introspectedTable.getRules().generateResultMapWithBLOBs()) {
            answer.addAttribute(new Attribute("resultMap", //$NON-NLS-1$
                    introspectedTable.getResultMapWithBLOBsId()));
        } else {
            answer.addAttribute(new Attribute("resultMap", //$NON-NLS-1$
                    introspectedTable.getBaseResultMapId()));
        }

        String parameterType;
        if (introspectedTable.getRules().generateRecordWithBLOBsClass()) {
            parameterType = introspectedTable.getRecordWithBLOBsType();
        } else {
            parameterType = introspectedTable.getBaseRecordType();
        }

        answer.addAttribute(new Attribute("parameterType", //$NON-NLS-1$
                parameterType));

        context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder();
        sb.append("select "); //$NON-NLS-1$
        
        answer.addElement(new TextElement(sb.toString()));
        answer.addElement(getBaseColumnListElement(introspectedTable));

        sb.setLength(0);
        sb.append("from "); //$NON-NLS-1$
        sb.append(introspectedTable
                .getAliasedFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        XmlElement includeBaseCondition = new XmlElement("include");
		includeBaseCondition.addAttribute(new Attribute("refid", "base_condition"));
		answer.addElement(includeBaseCondition);

        parentElement.addElement(answer);
    }
	
	protected void addSelectPageByConditionElement(XmlElement parentElement,IntrospectedTable introspectedTable) {
        XmlElement answer = new XmlElement("select"); //$NON-NLS-1$

        answer.addAttribute(new Attribute(
                "id", "selectPageByCondition")); //$NON-NLS-1$
        if (introspectedTable.getRules().generateResultMapWithBLOBs()) {
            answer.addAttribute(new Attribute("resultMap", //$NON-NLS-1$
                    introspectedTable.getResultMapWithBLOBsId()));
        } else {
            answer.addAttribute(new Attribute("resultMap", //$NON-NLS-1$
                    introspectedTable.getBaseResultMapId()));
        }

        context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder();
        sb.append("select "); //$NON-NLS-1$
        
        answer.addElement(new TextElement(sb.toString()));
        answer.addElement(getBaseColumnListElement(introspectedTable));

        sb.setLength(0);
        sb.append("from "); //$NON-NLS-1$
        sb.append(introspectedTable
                .getAliasedFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        XmlElement includeBaseCondition = new XmlElement("include");
		includeBaseCondition.addAttribute(new Attribute("refid", "base_condition_page"));
		answer.addElement(includeBaseCondition);
		
		sb.setLength(0);
        sb.append("limit #{pageStart,jdbcType=INTEGER},#{pageSize,jdbcType=INTEGER}");
        answer.addElement(new TextElement(sb.toString()));

        parentElement.addElement(answer);
    }
	
	protected void addSelectPageByConditionCountElement(XmlElement parentElement,IntrospectedTable introspectedTable) {
        XmlElement answer = new XmlElement("select");

        answer.addAttribute(new Attribute("id", "selectPageByConditionCount"));
        
        String parameterType;
        if (introspectedTable.getRules().generateRecordWithBLOBsClass()) {
            parameterType = introspectedTable.getRecordWithBLOBsType();
        } else {
            parameterType = introspectedTable.getBaseRecordType();
        }

        answer.addAttribute(new Attribute("parameterType",parameterType));
        
        answer.addAttribute(new Attribute("resultType", "java.lang.Integer"));

        context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder();
        sb.append("select count(1) from ");
        sb.append(introspectedTable
                .getAliasedFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        XmlElement includeBaseCondition = new XmlElement("include");
		includeBaseCondition.addAttribute(new Attribute("refid", "base_condition"));
		answer.addElement(includeBaseCondition);
		
        parentElement.addElement(answer);
    }
	
	protected XmlElement getBaseColumnListElement(IntrospectedTable introspectedTable) {
        XmlElement answer = new XmlElement("include"); //$NON-NLS-1$
        answer.addAttribute(new Attribute("refid",introspectedTable.getBaseColumnListId())); //$NON-NLS-1$
        return answer;
    }
	
	@Override
	public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
		PoseidonJavaGenerator javaGenerator = new PoseidonJavaGenerator();  
		javaGenerator.setContext(context);  
		javaGenerator.setIntrospectedTable(introspectedTable);  
        List<CompilationUnit> units = javaGenerator.getCompilationUnits();  
        List<GeneratedJavaFile> generatedJavaFiles = new ArrayList<GeneratedJavaFile>();
        for(CompilationUnit unit : units) {
        	generatedJavaFiles.add(new GeneratedJavaFile(unit,context.getJavaModelGeneratorConfiguration().getTargetProject(),context.getProperty("javaFileEncoding"),context.getJavaFormatter()));
        }
        return generatedJavaFiles;
	}

	@Override
	public boolean sqlMapGenerated(GeneratedXmlFile sqlMap, IntrospectedTable introspectedTable) {
		sqlMap.setMergeable(false);
		return super.sqlMapGenerated(sqlMap, introspectedTable);
	}

	@Override
	public boolean validate(List<String> warnings) {
		return true;
	}
}

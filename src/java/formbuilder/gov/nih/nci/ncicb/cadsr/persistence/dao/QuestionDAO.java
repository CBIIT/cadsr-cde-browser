package gov.nih.nci.ncicb.cadsr.persistence.dao;

import gov.nih.nci.ncicb.cadsr.exception.DMLException;
import gov.nih.nci.ncicb.cadsr.resource.Question;

import java.util.Collection;


public interface QuestionDAO {
  /**
   * Gets all the valid values for a question
   *
   * @param <b>questionId</b> Idseq of the question component.
   *
   * @return <b>Collection</b> List of form valid value objects
   */
  public Collection getValidValues(String questionId);

  /**
   * Creates a new question component (just the header info).
   *
   * @param <b>newQuestion</b> Question object
   *
   * @return <b>int</b> 1 - success, 0 - failure.
   *
   * @throws <b>DMLException</b>
   */
  public int createQuestionComponent(Question newQuestion)
    throws DMLException;

  /**
   * Creates new question components (just the header info).
   *
   * @param <b>questions</b> Collection of question objects
   *
   * @return <b>int</b> 1 - success, 0 - failure..
   *
   * @throws <b>DMLException</b>
   */
  public int createQuestionComponents(Collection questions)
    throws DMLException;

  /**
   * Adds new valid values to a question. 
   * 
   * Hyun: Don't implement this method
   *
   * @param <b>questionId</b> Idseq of the question component.
   * @param <b>validValues</b> FormValidValue objects to be added to the
   *        question
   *
   * @return <b>Question</b> Question object representing the question after
   *         addition of new validValues.
   *
   * @throws <b>DMLException</b>
   */
  public Question addValidValues(
    String questionId,
    Collection validValues) throws DMLException;

  /**
   * Deletes the specified question and all its associated components.
   *
   * @param <b>questionId</b> Idseq of the question component.
   *
   * @return <b>int</b> 1 - success, 0 - failure.
   *
   * @throws <b>DMLException</b>
   */
  public int deleteQuestion(String questionId) throws DMLException;

  /**
   * Changes the display order of the specified question. Display order of the
   * other questions the module is also updated accordingly.
   * 
   * Hyun: Use stored procedure: sbrext_form_builder_pkg.remove_question
   *
   * @param <b>questionId</b> Idseq of the question component.
   * @param <b>newDisplayOrder</b> New display order of the question component.
   *
   * @return <b>int</b> 1 - success, 0 - failure.
   *
   * @throws <b>DMLException</b>
   */
  public int updateDisplayOrder(
    String questionId,
    int newDisplayOrder) throws DMLException;

  /**
   * Changes the long name of a question.
   *
   * @param <b>questionId</b> Idseq of the question component.
   * @param <b>newLongName</b> New long name of the question component.
   *
   * @return <b>int</b> 1 - success, 0 - failure.
   *
   * @throws <b>DMLException</b>
   */
  public int updateQuestionLongName(
    String questionId,
    String newLongName) throws DMLException;

  /**
   * Changes the data element associated with a question.
   *
   * @param <b>questionId</b> Idseq of the question component.
   * @param <b>newDEId</b> Idseq of the data element associated with the
   *        question component.
   *
   * @return <b>int</b> 1 - success, 0 - failure.
   *
   * @throws <b>DMLException</b>
   */
  public int updateQuestionDEAssociation(
    String questionId,
    String newDEId) throws DMLException;

  /**
   * Changes the data element associated with a question.
   *
   * @param <b>questionId</b> Idseq of the question component.
   * @param <b>newDEId</b> Idseq of the data element associated with the
   *        question component.
   * @param <b>newLongName</b> New long name of the question component.
   *
   * @return <b>int</b> 1 - success, 0 - failure.
   *
   * @throws <b>DMLException</b>
   */
  public int updateQuestionDEAssociation(
    String questionId,
    String newDEId,
    String newLongName) throws DMLException;
}

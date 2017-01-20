package me.niccorder.spreadsheet.app.pres.impl;

import me.niccorder.spreadsheet.app.model.SpreadsheetModel;
import me.niccorder.spreadsheet.app.view.GridView;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class) public class CellGridPresenterImplTest {

  @Mock private SpreadsheetModel mMockSpreadsheetModel;

  @Mock private CellGridPresenterImpl mMockGridPresenterImpl;

  @Mock private GridView mView;

  @Test public void onCellClick() throws Exception {
    mMockGridPresenterImpl.setView(mView);
    mMockGridPresenterImpl.onCellClick(4, 2);

    assertThat(mMockGridPresenterImpl.currentFocus[0], is(4));
    assertThat(mMockGridPresenterImpl.currentFocus[1], is(2));
    assertThat(mMockGridPresenterImpl.isEditing, is(true));
  }

  @Test public void addRow() throws Exception {
    mMockGridPresenterImpl.setView(mView);
    mMockGridPresenterImpl.spreadsheetModel = mMockSpreadsheetModel;

    assertThat(mMockGridPresenterImpl.spreadsheetModel.getRows(), is(0));

    mMockGridPresenterImpl.addRow();
    mMockGridPresenterImpl.addRow();
    assertThat(mMockGridPresenterImpl.spreadsheetModel.getRows(), is(2));
  }

  @Test public void addColumn() throws Exception {

  }

  @Test public void undo() throws Exception {

  }
}
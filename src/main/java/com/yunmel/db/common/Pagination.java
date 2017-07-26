/**
 * Copyright 2010-2016 the original author or authors.
 * 
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yunmel.db.common;

import java.io.Serializable;
import java.util.List;

public class Pagination<T> implements Serializable {

  private static final long serialVersionUID = 1L;
  public static final int PAGESIZE = 20;// 默认每页记录数
  private int pageSize = PAGESIZE;// 页大小
  private long totalCount = 0;// 总记录数
  private int pageCount = 0;// 总页数
  private int startPage = 0;// 当前页
  private List<T> list = null;// 当前页数据

  public int getPageCount() {
    return pageCount;
  }

  public Pagination(int pagesize, long totalCount, int startPage, List<T> list) {
    super();
    setPagesize(pagesize);
    setTotalCount(totalCount);
    setStartPage(startPage);
    this.list = list;
  }

  public Pagination(long totalCount, List<T> list) {
    super();
    setPagesize(PAGESIZE);
    setTotalCount(totalCount);
    setStartPage(0);
    this.list = list;
  }

  public Pagination(int pagesize, long totalCount, List<T> list) {
    super();
    setPagesize(pagesize);
    setTotalCount(totalCount);
    setStartPage(0);
    this.list = list;
  }

  /*
   * 设置总页数
   */
  public void setPageCount(int pageCount) {
    this.pageCount = pageCount < 0 ? 0 : pageCount;
  }

  /*
   * 取页大小
   */
  public int getPagesize() {
    return pageSize;
  }

  /*
   * 设置每页大小，默认20
   */
  public void setPagesize(int pagesize) {
    this.pageSize = pagesize < 0 ? PAGESIZE : pagesize;
  }

  /*
   * 取当前页数
   */
  public int getStartPage() {
    return startPage;
  }

  /*
   * 设置当前页数
   */
  public void setStartPage(int startPage) {
    if (startPage < 0 || startPage >= pageCount) {
      this.startPage = 0;
    } else {
      this.startPage = startPage;
    }
  }

  /*
   * 取总记录数
   */
  public long getTotalCount() {
    return totalCount;
  }

  /**
   * 设置总记录条数
   */
  public void setTotalCount(long totalCount) {
    if (totalCount > 0) {
      this.totalCount = totalCount;
      setPageCount((int) Math.ceil((double) totalCount / pageSize));
    } else {
      this.totalCount = 0;
      setPageCount(0);
    }
  }

  /**
   * @return the list
   */
  public List<T> getList() {
    return list;
  }

  /**
   * @param list the list to set
   */
  public void setList(List<T> list) {
    this.list = list;
  }

  /**
   * 取下一页
   */
  public int getNextPage() {
    return startPage >= pageCount ? pageCount - 1 : startPage;
  }

  /**
   * 取前一页
   */
  public int getPreviousPage() {
    return startPage <= 0 ? 0 : startPage;
  }

  /**
   * 是否有下一页
   */
  public boolean hasNextPage() {
    return (getStartPage() < getPageCount() - 1);
  }

  /**
   * 是否有前一页
   */
  public boolean hasPreviousPage() {
    return (getStartPage() > 0);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#finalize()
   */
  @Override
  protected void finalize() throws Throwable {
    if (list != null)
      list = null;
    super.finalize();
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "Pagination [pageSize=" + pageSize + ", totalCount=" + totalCount + ", pageCount="
        + pageCount + ", startPage=" + startPage + ", list=" + list + "]";
  }
}
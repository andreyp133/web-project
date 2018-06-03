<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>ToDo</title>
    <style><%@include file='/bootstrap-3.3.7/css/bootstrap.css'%></style>
    <style><%@include file='/bootstrap-3.3.7/css/bootstrap-datetimepicker.min.css'%></style>
    <script src="/js/jquery-3.2.1.min.js"></script>
    <script src="/js/moment-with-locales.min.js"></script>
    <script src="/js/bootstrap.min.js"></script>
    <script src="/js/bootstrap-datetimepicker.min.js"></script>
    <script src="/js/checkNewSomdayTask.js"></script>
    <script src="/js/pickAllTasks.js"></script>
</head>
<body>
    <c:if test="${empty user}">
        <jsp:forward page="/index.jsp"/>
    </c:if>
    <header>
            <div class="row">
                <div class="col-xs-12" align="center">
                    <jsp:include page="/header.jsp"/>
                    <h4>Today is: ${date}</h4>
                    <form class="form-in-line" action="<c:url value='/operation/updatetasks'/>">
                        <input type="submit" class="btn btn-lg btn-primary" name="today" value="Today">
                        <input type="submit" class="btn btn-lg btn-primary" name="tomorrow" value="Tomorrow">
                        <input type="submit" class="btn btn-lg btn-primary" name="someday" value="Someday">
                        <input type="submit" class="btn btn-lg btn-primary" name="fixed" value="Fixed">
                        <input type="submit" class="btn btn-lg btn-primary" name="bin" value="Recycle Bin">
                    </form>
                </div>
            </div>
    </header>

    <article>
        <jsp:include page="/errorMessage.jsp"/>

        <!-- Today and Tomorrow tasks -->
        <c:if test="${section eq 'Today' || section eq 'Tomorrow' || section eq '' || empty section}">
            <div class="row">
                <div class="form-group col-xs-12" align="center">
                    <!-- Headers -->
                    <c:if test="${!isEmpty}">
                        <c:if test="${empty section}">
                            <h3>Today tasks:</h3>
                        </c:if>
                        <c:if test="${not empty section}">
                            <h3>${section} tasks:</h3>
                        </c:if>
                        <form class="form-in-line" method="post">
                        <div class="row">
                            <div class="form-group col-xs-2"></div>
                            <div class="form-group col-xs-8">
                                <!-- Table with tasks -->
                                <table class="table table-hover table-condensed">
                                    <!-- Headers -->
                                    <tr>
                                        <th class="not_mapped_style" style="text-align:center">
                                            <strong>Pick all</strong><br>
                                            <input type="checkbox" name="mainPicker"
                                                   onclick="pickAll();">
                                        </th>
                                        <th class="not_mapped_style" style="text-align:center">Task</th>
                                        <th class="not_mapped_style" style="text-align:center">File</th>
                                    </tr>
                                    <!-- Rows -->
                                    <c:forEach var="task" items="${tasks}">
                                        <tr>
                                            <!-- Fix or Unfix -->
                                            <td align="center">
                                                <h4>
                                                    <input type="checkbox" name="picker" value="${task.id}"
                                                           onclick="unpickMainCheckbox();">
                                                </h4>
                                            </td>
                                            <!-- Task -->
                                            <td align="center"><h4>${task.text}</h4></td>
                                            <!-- File upload -->
                                            <td align="center">
                                                <form></form><!-- Form for avoiding a strange bag -->
                                                <c:if test="${task.fileName eq ''}">
                                                    <form name="fileForm" method="post"
                                                          action="<c:url value='/operation/upload'/>"
                                                          enctype="multipart/form-data">
                                                        <input type="hidden" name="idTask" value="${task.id}">
                                                        <input type="file" class="form-control text-center"
                                                               name="file">
                                                        <input type="submit" class="btn btn-default"
                                                               name="add file" value="Add file">
                                                    </form>
                                                </c:if>
                                                <!-- If file already exists -->
                                                <c:if test="${not empty task.fileName}">
                                                    <form method="post">
                                                        <input type="hidden" name="idTask" value="${task.id}">
                                                        <input type="hidden" name="fileName" value="${task.fileName}">
                                                        <h4>${task.fileName}</h4>
                                                        <p><input type="submit" class="btn btn-default"
                                                                  name="download" value="Download"
                                                                  formaction="<c:url value='/operation/downloadFile'/>">
                                                            <input type="submit" class="btn btn-default"
                                                                   name="delete file" value="Delete file"
                                                                   formaction="<c:url value='/operation/deleteFile'/>">
                                                    </form>
                                                </c:if>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </table>
                            </div>
                        </div>
                        <!-- Buttons for actions -->
                        <div class="row">
                            <div class="form-group col-xs-2"></div>
                            <div class="form-group col-xs-8">
                                <input type="submit" class="btn btn-lg btn-success" name="fix" value="Fix"
                                       formaction="<c:url value='/operation/fix'/>">
                                <input type="submit" class="btn btn-lg btn-success" name="delete" value="Delete"
                                       formaction="<c:url value='/operation/delete'/>">
                            </div>
                        </div>
                        </form>
                    </c:if>
                </div>
            </div>
            <!-- Header, if all tasks were fixed -->
            <div class="row">
                <div class="col-sm-2"></div>
                <div class="col-sm-8" align="center">
                    <c:if test="${isEmpty}">
                        <h2><strong>All ${section} tasks were fixed!</strong></h2>
                    </c:if>
                </div>
            </div>

            <!-- Creating new task -->
            <div class="row">
                <div class="col-sm-2"></div>
                <div class="col-sm-8" align="center">
                    <details class="panel panel-default">
                        <summary class="panel-heading" style="cursor: pointer">Create new task</summary>
                            <div class="panel-body">
                                <form name="newTaskForm" method="post" action="<c:url value='/operation/create'/>"
                                      enctype="multipart/form-data">
                                    <h2><strong>Task:</strong></h2>
                                    <p><textarea class="form-control"
                                                 style="margin: 0px; width: 480px; height: 107px; resize: none;"
                                                 name="task" id="task"></textarea></p>
                                    <!-- File -->
                                    <div class="row">
                                        <div class="col-sm-3"></div>
                                        <div class="col-sm-6" align="center">
                                            <h4><strong>File:</strong>
                                                <input type="file" class="form-control" name="file">
                                            </h4>
                                        </div>
                                    </div>
                                    <!-- Buttons with different names!!! Explain type of further task! -->
                                    <c:if test="${section eq 'Today' || empty section}">
                                        <p><input type="submit" class="btn btn-primary" name="newTodayTask"
                                                  value="Create new task" onclick="return checkTextField()">
                                    </c:if>
                                    <c:if test="${section eq 'Tomorrow'}">
                                        <p><input type="submit" class="btn btn-primary" name="newTomorrowTask"
                                                  value="Create new task" onclick="return checkTextField()">
                                    </c:if>
                                </form>
                            </div>
                    </details>
                </div>
            </div>
        </c:if>

        <!-- Someday tasks -->
        <c:if test="${section eq 'Someday'}">
            <div class="row">
                <div class="form-group col-sx-12" align="center">
                    <c:if test="${!isEmpty}">
                        <!-- Name of tasks -->
                        <h3>${section} tasks:</h3>
                        <form method="post">
                            <div class="row">
                                <div class="form-group col-xs-2"></div>
                                <div class="form-group col-xs-8">
                                    <!-- Table with tasks -->
                                    <table class="table table-hover table-condensed">
                                        <!-- Headers -->
                                        <tr>
                                            <th class="not_mapped_style" style="text-align:center">
                                                <strong>Pick all</strong><br>
                                                <input type="checkbox" name="mainPicker"
                                                       onclick="pickAll();">
                                            </th>
                                            <th class="not_mapped_style" style="text-align:center">Task</th>
                                            <th class="not_mapped_style" style="text-align:center">Deadline</th>
                                            <th class="not_mapped_style" style="text-align:center">File</th>
                                        </tr>
                                        <!-- Rows -->
                                        <c:forEach var="task" items="${tasks}" >
                                            <tr>
                                                <!-- Fix or Delete -->
                                                <td align="center">
                                                    <h4>
                                                        <input type="checkbox" name="picker" value="${task.id}"
                                                               onclick="unpickMainCheckbox();">
                                                    </h4>
                                                </td>
                                                <!-- Task -->
                                                <td align="center"><h4>${task.text}</h4></td>
                                                <!-- Deadline -->
                                                <td align="center"><h4>${task.completionDate}</h4></td>
                                                <!-- File -->
                                                <td align="center">
                                                    <form></form><!-- Form for avoiding a strange bag -->
                                                    <!-- If file name exist -->
                                                    <c:if test="${task.fileName eq ''}"> <!-- If file name exist -->
                                                        <form name="fileForm" method="post"
                                                              action="<c:url value='/operation/upload'/>"
                                                              enctype="multipart/form-data">
                                                            <input type="hidden" name="idTask" value="${task.id}">
                                                            <input type="file" class="form-control text-center" name="file">
                                                            <p><input type="submit" class="btn btn-default"
                                                                      name="add file" value="Add file">
                                                        </form>
                                                    </c:if>
                                                    <!-- If file name not exist -->
                                                    <c:if test="${not empty task.fileName}">
                                                        <form method="post">
                                                            <input type="hidden" name="idTask" value="${task.id}">
                                                            <input type="hidden" name="fileName" value="${task.fileName}">
                                                            <h4>${task.fileName}</h4>
                                                            <p><input type="submit" class="btn btn-default"
                                                                      name="download" value="Download"
                                                                      formaction="<c:url value='/operation/downloadFile'/>">
                                                                <input type="submit" class="btn btn-default"
                                                                       name="delete file" value="Delete file"
                                                                       formaction="<c:url value='/operation/deleteFile'/>">
                                                        </form>
                                                    </c:if>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </table>
                                </div>
                            </div>
                            <!-- Buttons for fix and delete -->
                            <input type="submit" class="btn btn-lg btn-success" value="Fix"
                                   formaction="<c:url value='/operation/fix'/>">
                            <input type="submit" class="btn btn-lg btn-success" value="Delete"
                                   formaction="<c:url value='/operation/delete'/>">
                        </form>
                    </c:if>
                </div>
            </div>

            <!-- Header, if tasks not exist -->
            <div class="row">
                <div class="col-sm-2"></div>
                <div class="col-sm-8" align="center">
                    <c:if test="${isEmpty}">
                        <h2><strong>All ${section} tasks are fixed</strong></h2>
                    </c:if>
                </div>
            </div>

            <!-- Creating a new task -->
            <div class="row">
                <div class="col-sm-2"></div>
                <div class="col-sm-8" align="center">
                    <details class="panel panel-default">
                        <summary class="panel-heading" style="cursor: pointer">Create new task</summary>
                        <div class="panel-body">
                            <form name="newTaskForm" method="post" action="<c:url value='/operation/create'/>"
                                  enctype="multipart/form-data">
                                <!-- Header -->
                                <h2><strong>Task:</strong></h2>
                                <!-- Textarea -->
                                <p><textarea class="form-control"
                                             style="margin: 0px; width: 480px; height: 107px; resize: none;"
                                             name="task" id="somedayTask"></textarea></p>
                                <!-- File -->
                                <div class="row">
                                    <div class="col-sm-3"></div>
                                    <div class="col-sm-6" align="center">
                                        <h4><strong>File:</strong>
                                            <input type="file" class="form-control" name="file">
                                        </h4>
                                    </div>
                                </div>
                                <!-- Date -->
                                <div class="row">
                                    <div class="col-sm-4"></div>
                                    <div class="col-sm-4" align="center">
                                        <h4><strong>Date:</strong>
                                            <div class="input-group date" id="datetimepicker">
                                                <input type="text" class="form-control" name="date" id="date" value="Date" />
                                                <span class="input-group-addon">
				                                    <span class="glyphicon-calendar glyphicon"></span>
			                                    </span>
                                            </div>
                                        </h4>
                                    </div>
                                </div>
                                <!-- New tasks button -->
                                <br><input type="submit" class="btn btn-lg btn-primary" name="newSomedayTask"
                                          value="Create new task" onclick="return checkNewSomdayTask();">
                            </form>
                        </div>
                    </details>
                </div>
            </div>
        </c:if>

        <!-- Fixed tasks -->
        <c:if test="${section eq 'Fixed'}">
            <div class="row">
                <div class="form-group col-sx-12" align="center">
                    <c:if test="${!isEmpty}">
                        <!-- Name of tasks -->
                        <h3>${section} tasks:</h3>
                        <form method="post">
                            <div class="row">
                                <div class="form-group col-xs-2"></div>
                                <div class="form-group col-xs-8">
                                    <!-- Table with tasks -->
                                    <table class="table table-hover table-condensed">
                                        <!-- Headers -->
                                        <tr>
                                            <th class="not_mapped_style" style="text-align:center">
                                                <strong>Pick all</strong><br>
                                                <input type="checkbox" name="mainPicker"
                                                       onclick="pickAll();">
                                            </th>
                                            <th class="not_mapped_style" style="text-align:center">Task</th>
                                            <th class="not_mapped_style" style="text-align:center">Deadline</th>
                                            <th class="not_mapped_style" style="text-align:center">File</th>
                                        </tr>
                                        <!-- Rows -->
                                        <c:forEach var="task" items="${tasks}" >
                                            <tr>
                                                <!-- Unfix or Delete -->
                                                <td align="center">
                                                    <h4>
                                                        <input type="checkbox" name="picker" value="${task.id}"
                                                               onclick="unpickMainCheckbox();">
                                                    </h4>
                                                </td>
                                                <!-- Task -->
                                                <td align="center"><h4>${task.text}</h4></td>
                                                <!-- Date -->
                                                <td align="center"><h4>${task.completionDate}</h4></td>
                                                <!-- File -->
                                                <td align="center">
                                                    <c:if test="${not empty task.fileName}">
                                                        <input type="hidden" name="fileName" value="${task.fileName}">
                                                        <h4>${task.fileName}</h4>
                                                        <input type="submit" class="btn btn-default"
                                                               name="download" value="download"
                                                               formaction="<c:url value='/operation/downloadFile'/>">
                                                    </c:if>
                                                    <c:if test="${task.fileName eq ''}">
                                                        <h4><strong>This task without file</strong></h4>
                                                    </c:if>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </table>
                                </div>
                            </div>

                            <input type="submit" class="btn btn-lg btn-success" value="Unfix"
                                   formaction="<c:url value='/operation/unfix'/>">
                            <input type="submit" class="btn btn-lg btn-success" value="Delete"
                                   formaction="<c:url value='/operation/delete'/>">
                        </form>
                    </c:if>

                    <!-- Header, if tasks not exist -->
                    <div class="row">
                        <div class="col-sm-2"></div>
                        <div class="col-sm-8" align="center">
                            <c:if test="${isEmpty}">
                                <h2><strong>No completed tasks</strong></h2>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>
        </c:if>

        <!-- Deleted to recycle byn tasks -->
        <c:if test="${section eq 'Bin'}">
            <div class="row">
                <div class="form-group col-sx-12" align="center">
                    <c:if test="${!isEmpty}">
                        <!-- Name of tasks -->
                        <h3>Deleted tasks:</h3>
                        <form method="post">
                            <div class="row">
                                <div class="form-group col-xs-2"></div>
                                <div class="form-group col-xs-8">
                                    <!-- Table with tasks -->
                                    <table class="table table-hover table-condensed">
                                        <!-- Headers -->
                                        <tr>
                                            <th class="not_mapped_style" style="text-align:center">
                                                <strong>Pick all</strong><br>
                                                <input type="checkbox" name="mainPicker"
                                                       onclick="pickAll();">
                                            </th>
                                            <th class="not_mapped_style" style="text-align:center">Task</th>
                                            <th class="not_mapped_style" style="text-align:center">Deadline</th>
                                            <th class="not_mapped_style" style="text-align:center">File</th>
                                        </tr>
                                        <!-- Rows -->
                                        <c:forEach var="task" items="${tasks}" >
                                            <tr>
                                                <!-- Undelete -->
                                                <td align="center">
                                                    <input type="hidden" name="fileName" value="${task.fileName}">
                                                    <h4>
                                                        <input type="checkbox" name="picker" value="${task.id}"
                                                               onclick="unpickMainCheckbox();">
                                                    </h4>
                                                </td>
                                                <!-- Task -->
                                                <td align="center"><h4>${task.text}</h4></td>
                                                <!-- Date -->
                                                <td align="center"><h4>${task.completionDate}</h4></td>
                                                <!-- File -->
                                                <td align="center">
                                                    <c:if test="${not empty task.fileName}"><h4>${task.fileName}</h4></c:if>
                                                    <c:if test="${task.fileName eq ''}">
                                                        <h4><strong>This task without file</strong></h4>
                                                    </c:if>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </table>
                                </div>
                            </div>

                            <input type="submit" class="btn btn-lg btn-success" value="Restore"
                                   formaction="<c:url value='/operation/restore'/>">
                            <input type="submit" class="btn btn-lg btn-success" value="Delete"
                                   onclick="return confirm('You\'re confident? Task will delete permanently.')"
                                   formaction="<c:url value='/operation/destroy'/>">
                            <input type="submit" class="btn btn-lg btn-success" name="destroyAll"
                                   value="Empty recycle bin"
                                   onclick="return confirm('You\'re confident? All tasks will delete permanently.')"
                                   formaction="<c:url value='/operation/destroy'/>">
                        </form>
                    </c:if>

                    <!-- Header, if tasks not exist -->
                    <div class="row">
                        <div class="col-sm-2"></div>
                        <div class="col-sm-8" align="center">
                            <c:if test="${isEmpty}">
                                <h2><strong>Recycle Bin is empty!</strong></h2>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>
        </c:if>
    </article>

    <footer>
        <%@ include  file="/footer.html"%>
    </footer>
</body>
</html>
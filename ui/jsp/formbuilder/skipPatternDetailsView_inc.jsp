                                                          <tr class="OraTabledata">
                                                           <td>
                                                             <table width="100%" align="center" cellpadding="0" cellspacing="0" border="0" >
                                                               <tr>
                                                                 <td class="OraTableColumnHeader" width="100%" nowrap>
                                                                   Skip to
                                                                 </td>
                                                                </tr>
                                                              </table>
                                                             </td>
                                                          </tr>
                                                          <tr>
                                                            <td>
                                                              <table width="100%" align="center" cellpadding="0" cellspacing="1" border="0" >
                                                
                                                             <logic:equal value="<%=FormJspUtil.FORM%>" name="skipTargetType">
                                                             <tr class="OraTabledata">
                                                              <td class="OraTableColumnHeader" width="20%" nowrap>
                                                                Form <bean:message key="cadsr.formbuilder.form.longName"/>
                                                              </td>
                                                              <td  class="OraFieldText" width="80%" nowrap>
                                                                    <bean:write  name="skipTarget" property="longName"/> 
                                                               </td>            
                                                             </tr>               
                                                             </logic:equal>
                                                             
                                                             <logic:equal value="<%=FormJspUtil.MODULE%>" name="skipTargetType">
                                                              <tr class="OraTabledata">
                                                              <td class="OraTableColumnHeader" width="20%" nowrap>
                                                                Module Name
                                                              </td>
                                                              <td  class="OraFieldText" width="80%" nowrap>
                                                                    <bean:write  name="skipTarget" property="longName"/> 
                                                               </td>                                                               
                                                             </tr>   
                                                             <tr class="OraTabledata">
                                                              <td class="OraTableColumnHeader" width="20%" nowrap>
                                                                Form <bean:message key="cadsr.formbuilder.form.longName"/>
                                                              </td>
                                                              <td  class="OraFieldText" width="80%" nowrap>
                                                                    <bean:write  name="skipTarget" property="form.longName"/> 
                                                               </td>
                                                             </tr>               
                                                             </logic:equal>   
                                                             
                                                             <logic:equal value="<%=FormJspUtil.QUESTION%>" name="skipTargetType">
                                                              <tr class="OraTabledata">
                                                              <td class="OraTableColumnHeader" width="20%" nowrap>
                                                                Question Name
                                                              </td>
                                                              <logic:present name="skipTarget" property="dataElement">
                                                              <td  class="OraFieldText" width="80%" nowrap>
                                                                <table width="100%">
                                                                  <tr>
                                                                    <td  width="90%" class="OraFieldText" >
                                                                      <bean:write  name="skipTarget" property="longName"/> 
                                                                    </td>
                                                                   <td width="5%" align=right>
                                                                        <html:link page='<%="/search?dataElementDetails=9&PageId=DataElementsGroup&queryDE=yes"%>'
                                                                                paramId = "p_de_idseq"
                                                                                paramName="skipTarget"
                                                                                paramProperty="dataElement.deIdseq"
                                                                                target="_blank">
                                                                         <bean:write name="skipTarget" property="dataElement.CDEId"/>
                                                                       </html:link> 
                                                                    </td>    
                                                                    <td  width="5%" class="OraFieldText" nowrap align=right>
                                                                      <bean:write name="skipTarget" property="dataElement.version"/>
                                                                    </td>                    
                                                                  </tr>
                                                                 </table>
                                                              </td>
                                                               </logic:present>
                                                  
                                                              <logic:notPresent name="skipTarget" property="dataElement">
                                                               <td  class="OraFieldText" width="70%" >
                                                                <bean:write  name="skipTarget" property="longName"/> 
                                                                </td>                   
                                                              </logic:notPresent>                 
                                                             </tr>   
                                                              <tr class="OraTabledata">
                                                              <td class="OraTableColumnHeader" width="20%" nowrap>
                                                                Module Name
                                                              </td>
                                                              <td  class="OraFieldText" width="80%" nowrap>
                                                                    <bean:write  name="skipTarget" property="module.longName"/> 
                                                               </td>                
                                                             </tr>   
                                                             <tr class="OraTabledata">
                                                              <td class="OraTableColumnHeader" width="20%" nowrap>
                                                                Form <bean:message key="cadsr.formbuilder.form.longName"/>
                                                              </td>
                                                              <td  class="OraFieldText" width="80%" nowrap>
                                                                    <bean:write  name="skipTarget" property="module.form.longName"/> 
                                                               </td>                   
                                                             </tr>       
                                                             <tr class="OraTabledata">
                                                              <td class="OraTableColumnHeader" width="20%" nowrap>
                                                                Protocols
                                                              </td>
                                                              <td  class="OraFieldText" width="80%" nowrap>
                                                                    CTMS Version 3.0, NETTRIALS 
                                                               </td>
                  
                                                             </tr>    
                                                             <tr class="OraTabledata">
                                                              <td class="OraTableColumnHeader" width="20%" nowrap>
                                                                Classifications
                                                              </td>
                                                              <td  class="OraFieldText" width="80%" nowrap>
                                                                   caBIG, Multiple Myeloma
                                                               </td>                  
                                                             </tr>                                                                
                                                             </logic:equal>   
                                                             
                                                             <tr class="OraTabledata">
                                                              <td class="OraTableColumnHeader" width="20%" nowrap>
                                                                Instruction
                                                              </td>
                                                              <td  class="OraFieldText" width="80%" nowrap>
                                                                    skip to target 
                                                               </td>             
                                                             </tr>                                                  
                                                                                                               
                                                                </table>
                                                             </td>
                                                            </tr>                                                            
Hello,
Notification. The following user account expired: 
<#if users??>
<#list users as user>
**********************************
 User:  ${user.name} <#if user.attribute??><#list user.attribute as attribute><#if attribute.name="company">
 Company: ${attribute.value}</#if><#if attribute.name="email">
 Email: ${attribute.value}</#if><#if attribute.name="expires">
 Expiring Date: ${attribute.value}</#if></#list></#if>
</#list>
**********************************
</#if>
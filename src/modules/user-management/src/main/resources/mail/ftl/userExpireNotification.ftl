Hi ${user.name},
Your account expired <#if user.attribute??><#list user.attribute as attribute><#if attribute.name="expires"> at ${attribute.value}</#if></#list></#if>
If you want to renew you Account please contact <administrator-email>

Best Regards,
GeoSolutions S.a.s.


  
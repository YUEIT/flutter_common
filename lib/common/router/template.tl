{% for path in imports %}
import "{{ path }}";
{% endfor %}
  class ARouterMap {
    static final Map<String, List<Map<String, dynamic>>> innerRouterMap = <String, List<Map<String, dynamic>>>
      {{ routerStr }};

    static dynamic instanceFromClazz(Type clazz, dynamic option) {
      {{ routerClazz }}
      switch (clazz) {
        {% for router in routerList %}
          case {{ router }}:
            return new {{ router }}(option);
        {% endfor %}
        default:return null;
      }
    }
  }
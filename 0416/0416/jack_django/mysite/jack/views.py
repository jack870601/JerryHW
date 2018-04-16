from django.shortcuts import render_to_response
from django.http import HttpResponse
#from .models import Restaurant,Food
from .models import Introduction
# Create your views here.
def index(request):	
	introductions = Introduction.objects.all()
	return render_to_response('jack/menu.html',locals())
